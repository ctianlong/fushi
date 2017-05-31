package springboot.util;

import java.util.Random;

/**
 * 
 * @author Tianlong
 *
 */
public class RandomUtil
{
    private static final Random RANDOM = new Random();
    /**
     * 产生两个数之间的随机数
     * @param min 小数
     * @param max 比min大的数
     * @return int 随机数字
     */
    public static int num(int min, int max)
    {
        return min + RANDOM.nextInt(max - min);
    }

    /**
     * 产生0--num的随机数,不包括num
     * @param num 数字
     * @return int 随机数字
     */
    public static int num(int num)
    {
        return RANDOM.nextInt(num);
    }

}