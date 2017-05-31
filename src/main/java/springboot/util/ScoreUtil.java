package springboot.util;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import springboot.domain.CompInteScoItem;
import springboot.domain.Student;

/**
 * 计算学生成绩的工具类
 * @author Tianlong
 *
 */
public final class ScoreUtil {

	private ScoreUtil(){}
	
	//默认保存的精度
	private static final int DEF_SCALE = 2;
	
	//总分所需的精度
	private static final int SUM_SCALE = 0;
	
	/**
	 * 计算单个学生总成绩，总是会计算一遍综合面试成绩
	 * @param s
	 * @return
	 */
	public static boolean CalculateOne (Student s) {
		return CalculateOne(s, true);
	}
	
	/**
	 * 计算单个学生总成绩
	 * @param s
	 * @param calculateInte 决定是否需要计算一遍综合面试成绩
	 * @return
	 */
	public static boolean CalculateOne (Student s, boolean calculateInte) {
		if (calculateInte ? generateCompInteScore(s) : s.getCompInteTotScore() != null) {
			//同时需要判断前三项成绩不为空
			Byte pcs = s.getProClaScore();
			Byte ews = s.getEngWirScore();
			Byte ess = s.getEngSpeScore();
			if (pcs != null && ews != null && ess != null) {
				BigDecimal ots = s.getCompInteTotScore().add(new BigDecimal(pcs + ews + ess));
				s.oriTotScore(ots).lasTotScore(ots.divide(new BigDecimal("0.7"), SUM_SCALE, BigDecimal.ROUND_HALF_UP));
				return true;
			}
		}
		s.oriTotScore(null).lasTotScore(null);
		return false;
	}
	
	/**
	 * 计算单个考生的综合面试单项分数（30*5）及总分（150）
	 * @param s
	 * @return
	 */
	public static boolean generateCompInteScore(Student s){
		Set<CompInteScoItem> items = s.getItems();
		int itemNum = items.size();
		//起码有三个老师打分才能去掉最高和最低
		if(itemNum > 2){
			//检查教师评分是否完整
			//此处只检查总分是否存在，减少服务器负担，如果不考虑负担，最好能够把小项分也检查一遍
			boolean isCompleted = items.stream().allMatch(i -> i.getCompInteScoSum() != null);
			//如果完整，则计算
			if(isCompleted){
				Set<CompInteScoItem> vaildItem = items.stream().sorted(Comparator.comparingInt(CompInteScoItem::getCompInteScoSum)).limit(itemNum - 1).skip(1).collect(Collectors.toSet());
				BigDecimal ss = computeItemScore(vaildItem, SUM_SCALE, CompInteScoItem::getCompInteScoSum);
				BigDecimal s1 = computeItemScore(vaildItem, DEF_SCALE, CompInteScoItem::getCompInteSco1);
				BigDecimal s2 = computeItemScore(vaildItem, DEF_SCALE, CompInteScoItem::getCompInteSco2);
				BigDecimal s3 = computeItemScore(vaildItem, DEF_SCALE, CompInteScoItem::getCompInteSco3);
				BigDecimal s4 = computeItemScore(vaildItem, DEF_SCALE, CompInteScoItem::getCompInteSco4);
				BigDecimal s5 = computeItemScore(vaildItem, DEF_SCALE, CompInteScoItem::getCompInteSco5);
				s.compInteSco1(s1).compInteSco2(s2).compInteSco3(s3).compInteSco4(s4).compInteSco5(s5).compInteTotScore(ss);
				return true;
			}
		}
		s.compInteSco1(null).compInteSco2(null).compInteSco3(null).compInteSco4(null).compInteSco5(null).compInteTotScore(null);
		return false;
	}
	
	/**
	 * 计算items 中选定项的平均分，需要指定精度
	 * @param items
	 * @param itemNum
	 * @param mapper
	 * @return
	 */
	private static BigDecimal computeItemScore(Set<CompInteScoItem> items, int scale, ToIntFunction<? super CompInteScoItem> mapper){
		//四舍五入，可以通过设置精度，也可以调用 Math.round方法
		//计算过程也可以通过算出总和，全部用 BigDecimal 包装计算
		return new BigDecimal(Double.toString(items.stream().mapToInt(mapper).average().getAsDouble())).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
}
