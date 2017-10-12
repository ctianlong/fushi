package springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import springboot.domain.CompInteScoItem;
import springboot.domain.Student;
import springboot.domain.User;
import springboot.repository.CompInteScoItemRepository;
import springboot.repository.StudentRepository;
import springboot.repository.UserRepository;
import springboot.security.RoleConstants;
import springboot.security.SecurityUtil;
import springboot.service.mapper.CompInteScoItemMapper;
import springboot.util.RandomUtil;
import springboot.util.ScoreUtil;

@Controller
public class CompInteScoItemController {
	
	private final CompInteScoItemMapper itemMapper;
	
	private final CompInteScoItemRepository itemRepository;
	
	private final StudentRepository studentRepository;
	
	private final UserRepository userRepository;
	
	public CompInteScoItemController(CompInteScoItemRepository itemRepository, CompInteScoItemMapper itemMapper,
		StudentRepository studentRepository, UserRepository userRepository) {
		this.itemRepository = itemRepository;
		this.itemMapper = itemMapper;
		this.studentRepository = studentRepository;
		this.userRepository = userRepository;
	}

	/**
	 * 当前打分老师的待录入综合面试打分记录列表
	 * @param model
	 * @return
	 */
	@GetMapping("/teacher/comp-inte-sco-items")
	public String itemListByCurrentTeacher(Model model){
		if (SecurityUtil.getCurrentUser().getAgreed().equals(false)) {
			return "redirect:/403";
		}
		// 下述两种方式都会产生一条额外的通过userID查询User的sql语句，不解。
		model.addAttribute("list", itemRepository.findWithStudentByTeacherIsCurrentUser());
//		model.addAttribute("list", itemRepository.findAllWithStudentByTeacherId(SecurityUtil.getCurrentUid()));
		return "comp-inte-sco-item/teacher/list";
	}
	
	/**
	 * 教师综合面试打分的编辑表单页面
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/teacher/comp-inte-sco-items/{id}/form")
	public String itemUpdateFormForTeacher(@PathVariable Long id, Model model){
		model.addAttribute("item", itemRepository.findOneWithStudentById(id));
		String api = "/teacher/comp-inte-sco-items/" + id + "/update";
		model.addAttribute("api", api);
		return "comp-inte-sco-item/teacher/form";
	}
	
	/**
	 * 教师打分，综合面试
	 * @param id
	 * @param student
	 * @return
	 */
	@PostMapping("/teacher/comp-inte-sco-items/{id}/update")
	public String updateItemForTeacher(@PathVariable Long id, CompInteScoItem item){
		if (SecurityUtil.getCurrentUser().getSubmitted().equals(true)) {
			return "redirect:/403";
		}
		Byte s1 = item.getCompInteSco1();
		Byte s2 = item.getCompInteSco2();
		Byte s3 = item.getCompInteSco3();
		Byte s4 = item.getCompInteSco4();
		Byte s5 = item.getCompInteSco5();
		Short sum = item.getCompInteScoSum();
		if (checkScore(0, 30, sum, s1, s2, s3, s4, s5)) {
			CompInteScoItem oi = itemRepository.findOne(id);
			if(oi != null){
				oi.setCompInteSco1(s1);
				oi.setCompInteSco2(s2);
				oi.setCompInteSco3(s3);
				oi.setCompInteSco4(s4);
				oi.setCompInteSco5(s5);
				oi.setCompInteScoSum(sum);
				itemRepository.flush();
				//每次修改后触发更新，（每次修改都会查数据库，开销大，后期考虑改进）
				Student student = studentRepository.findOneWithItemsById(oi.getStudent().getId());
				ScoreUtil.CalculateOne(student);
				studentRepository.flush();
			}
		}
		return "redirect:/teacher/comp-inte-sco-items";
	}
	
	private boolean checkScore(int min, int max, Short sum, final Byte... scores) {
		short ss = 0;
		for (final Byte score : scores) {
			if (score == null || score < min || score > max) return false;
			ss += score;
		}
		if (sum == null || !sum.equals(ss)) return false;
		return true;
	}
	
	/**
	 * 教师重置学生综合面试成绩
	 * @param id
	 */
	@PutMapping("/teacher/comp-inte-sco-items/{id}/reset")
	@ResponseBody
	public ResponseEntity<?> resetItemForTeacher(@PathVariable Long id){
		CompInteScoItem item = itemRepository.findOne(id);
		if(item != null){
			item.setCompInteSco1(null);
			item.setCompInteSco2(null);
			item.setCompInteSco3(null);
			item.setCompInteSco4(null);
			item.setCompInteSco5(null);
			item.setCompInteScoSum(null);
			itemRepository.flush();
			//每次修改后触发更新，（每次修改都会查数据库，开销大，后期考虑改进）
			Student student = studentRepository.findOneWithItemsById(item.getStudent().getId());
			ScoreUtil.CalculateOne(student);
			studentRepository.flush();
		}
		return ResponseEntity.ok().header("X-app-alert", "item.reset.success")
				.header("X-app-params", id.toString()).build();
	}
	
	/**
	 * 管理学生综合面试成绩的表单页面
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/manager/comp-inte-sco-items/{id}/form")
	public String itemUpdateFormForManager(@PathVariable Long id, Model model){
		model.addAttribute("item", itemRepository.findOneWithStudentWithTeacherById(id));
		model.addAttribute("api", "/manager/comp-inte-sco-items/" + id + "/update");
		return "comp-inte-sco-item/manager/form";
	}
	
	/**
	 * 管理学生综合面试成绩修改
	 * @param id
	 * @param item
	 * @return
	 */
	@PostMapping("/manager/comp-inte-sco-items/{id}/update")
	public String updateItemForManager(@PathVariable Long id, CompInteScoItem item){
		Byte s1 = item.getCompInteSco1();
		Byte s2 = item.getCompInteSco2();
		Byte s3 = item.getCompInteSco3();
		Byte s4 = item.getCompInteSco4();
		Byte s5 = item.getCompInteSco5();
		Short sum = item.getCompInteScoSum();
		CompInteScoItem oi = null;
		if (checkScore(0, 30, sum, s1, s2, s3, s4, s5)) {
			oi = itemRepository.findOne(id);
			if(oi != null){
				oi.setCompInteSco1(s1);
				oi.setCompInteSco2(s2);
				oi.setCompInteSco3(s3);
				oi.setCompInteSco4(s4);
				oi.setCompInteSco5(s5);
				oi.setCompInteScoSum(sum);
				itemRepository.flush();
				//每次修改后触发更新，（每次修改都会查数据库，开销大，后期考虑改进）
				Student student = studentRepository.findOneWithItemsById(oi.getStudent().getId());
				ScoreUtil.CalculateOne(student);
				studentRepository.flush();
			}
		}
		return "redirect:/students/score/" + oi.getStudent().getId() + "/detail";
	}
	
	/**
	 * 后台管理重置学生综合面试成绩
	 * @param id
	 */
	@PutMapping("/manager/comp-inte-sco-items/{id}/reset")
	@ResponseBody
	public ResponseEntity<?> resetItemForManager(@PathVariable Long id){
		CompInteScoItem item = itemRepository.findOne(id);
		if(item != null){
			item.setCompInteSco1(null);
			item.setCompInteSco2(null);
			item.setCompInteSco3(null);
			item.setCompInteSco4(null);
			item.setCompInteSco5(null);
			item.setCompInteScoSum(null);
			itemRepository.flush();
			//每次修改后触发更新，（每次修改都会查数据库，开销大，后期考虑改进）
			Student student = studentRepository.findOneWithItemsById(item.getStudent().getId());
			ScoreUtil.CalculateOne(student);
			studentRepository.flush();
		}
		return ResponseEntity.ok().header("X-app-alert", "item.reset.success")
				.header("X-app-params", id.toString()).build();
	}
	
	@PostMapping("/manager/comp-inte-sco-items/initAll")
	@ResponseBody
	public ResponseEntity<?> initAllItems() {
		itemRepository.deleteAllInBatch();
		List<User> teachers = userRepository.findAllByRolesName(RoleConstants.USER);
		List<Student> students = studentRepository.findAll();
		
		students.parallelStream()
			.flatMap((s) -> {
				Byte groupNo = s.getGroupNo();
				// 考虑还未分配组号的情况
				if (groupNo == null) {
//					return Stream.<CompInteScoItem>of();
					return null;
				}
				return teachers.parallelStream()
					.filter((t) -> groupNo.equals(t.getGroupNo()))
					.map((t) -> new CompInteScoItem(t, s));
			})
			.forEach(itemRepository::save);
		
		students.parallelStream().forEach((s) -> {
			s.compInteSco1(null)
				.compInteSco2(null)
				.compInteSco3(null)
				.compInteSco4(null)
				.compInteSco5(null)
				.compInteTotScore(null)
				.oriTotScore(null)
				.lasTotScore(null);
		});
		studentRepository.flush();
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/manager/comp-inte-sco-items/initPart")
	@ResponseBody
	public ResponseEntity<?> initPartItems() {
		List<User> teachers = userRepository.findAllByRolesName(RoleConstants.USER);
		List<Student> students = studentRepository.findAll();
		List<CompInteScoItem> oldItems = itemRepository.findAll();
		
		List<CompInteScoItem> targetItems = students.parallelStream()
			.flatMap((s) -> {
				Byte groupNo = s.getGroupNo();
				// 考虑还未分配组号的情况
				if (groupNo == null) {
//					return Stream.<CompInteScoItem>of();
					return null;
				}
				return teachers.parallelStream()
						.filter((t) -> groupNo.equals(t.getGroupNo()))
						.map((t) -> new CompInteScoItem(t, s));
			}).collect(Collectors.toList());
		
		List<CompInteScoItem> deleteItems = oldItems.parallelStream()
			.filter((oi) -> 
				targetItems.parallelStream()
					.noneMatch((ti) -> 
						oi.getStudent().getId().equals(ti.getStudent().getId()) && oi.getTeacher().getId().equals(ti.getTeacher().getId())
					)
			).collect(Collectors.toList());
		
		List<CompInteScoItem> addItems = targetItems.parallelStream()
			.filter((ti) -> 
				oldItems.parallelStream()
					.noneMatch((oi) -> 
						oi.getStudent().getId().equals(ti.getStudent().getId()) && oi.getTeacher().getId().equals(ti.getTeacher().getId())
					)
			).collect(Collectors.toList());
		
		itemRepository.deleteInBatch(deleteItems);
		itemRepository.save(addItems);
		itemRepository.flush();
		
		List<Student> newStudents = studentRepository.findAllWithItems();
		newStudents.parallelStream().forEach(ScoreUtil::CalculateOne);
		studentRepository.flush();
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/manager/comp-inte-sco-items/randomScore")
	@ResponseBody
	public ResponseEntity<?> randomItemScore() {
		
		itemRepository.findAll().parallelStream()
			.forEach((i) -> {
				i.compInteSco1((byte) RandomUtil.num(18, 30))
					.compInteSco2((byte) RandomUtil.num(18, 30))
					.compInteSco3((byte) RandomUtil.num(18, 30))
					.compInteSco4((byte) RandomUtil.num(18, 30))
					.compInteSco5((byte) RandomUtil.num(18, 30))
					.compInteScoSum((short) (i.getCompInteSco1() + i.getCompInteSco2() + i.getCompInteSco3() + i.getCompInteSco4() + i.getCompInteSco5()));
			});
		itemRepository.flush();
		
		List<Student> students = studentRepository.findAllWithItems();
		students.parallelStream().forEach((s) -> {
			s.proClaScore((byte) RandomUtil.num(60, 100))
				.engSpeScore((byte) RandomUtil.num(30, 50))
				.engWirScore((byte) RandomUtil.num(30, 50));
			ScoreUtil.CalculateOne(s);
		});
		studentRepository.flush();
		
		return ResponseEntity.ok().build();
	}
	
	
	
	
}
