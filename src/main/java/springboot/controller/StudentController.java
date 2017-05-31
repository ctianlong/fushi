package springboot.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import springboot.domain.Student;
import springboot.repository.CompInteScoItemRepository;
import springboot.repository.StudentRepository;
import springboot.service.mapper.StudentMapper;
import springboot.util.ScoreUtil;

@Controller
@RequestMapping("/students")
public class StudentController {
	
	private final StudentRepository studentRepository;
	
	private final CompInteScoItemRepository itemRepository;
	
	private final StudentMapper studentMapper;
	
	public StudentController(StudentRepository studentRepository, CompInteScoItemRepository itemRepository, StudentMapper studentMapper) {
		this.studentMapper = studentMapper;
		this.studentRepository = studentRepository;
		this.itemRepository = itemRepository;
	}

	/**
	 * 考生基本信息列表
	 * @param model
	 * @return
	 */
	@GetMapping("/info")
	public String studentInfoList(Model model){
		model.addAttribute("stuList", studentRepository.findAll());
		return "student/info/list";
	}
	
	/**
	 * 添加考生基本信息表单页面
	 * @param model
	 * @return
	 */
	@GetMapping("/info/form")
	public String studentAddForm(Model model){
		model.addAttribute("api", "/students/info/add");
		return "student/info/form";
	}
	
	/**
	 * 添加考生基本信息
	 * @param student
	 * @return
	 */
	@PostMapping("/info/add")
	public String createStudent(Student student){
		if(StringUtils.isNotBlank(student.getFullname())){
			studentRepository.saveAndFlush(student);
		}
		return "redirect:/students/info";
	}
	
	/**
	 * 修改考生基本信息表单页面
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/info/{id}/form")
	public String studentInfoUpdateForm(@PathVariable Long id, Model model){
		model.addAttribute("student", studentRepository.findOne(id));
		String api = "/students/info/" + id + "/update";
		model.addAttribute("api", api);
		return "student/info/form";
	}
	
	/**
	 * 修改考生基本信息
	 * @param id
	 * @param student
	 * @return
	 */
	@PostMapping("/info/{id}/update")
	public String updateStudentInfo(@PathVariable Long id, Student student){
		Student os = studentRepository.findOne(id);
		if (os != null && StringUtils.isNotBlank(student.getFullname())) {
			os.setFullname(student.getFullname());
			os.setGroupNo(student.getGroupNo());
			os.setGraduatedCollege(student.getGraduatedCollege());
			os.setFirstTutor(student.getFirstTutor());
			os.setSecondTutor(student.getSecondTutor());
			os.setTelephone(student.getTelephone());
			os.setHomeAddress(student.getHomeAddress());
			studentRepository.flush();
		}
		return "redirect:/students/info";
	}
	
	/**
	 * 删除考生基本信息
	 * @param id
	 */
	@DeleteMapping("/info/{id}/delete")
	@ResponseBody
	public ResponseEntity<?> deleteStudent(@PathVariable Long id){
		try {
			studentRepository.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().header("X-app-error", "student.delete.error")
					.header("X-app-params", id.toString()).build();
		}
		return ResponseEntity.ok().header("X-app-alert", "student.delete.success")
				.header("X-app-params", id.toString()).build();
	}
	
	
	/**
	 * 考生成绩列表
	 * @param model
	 * @return
	 */
	@GetMapping("/score")
	public String studentScoreList(Model model){
		model.addAttribute("stuList", studentRepository.findAll());
		return "student/score/list";
	}
	
	/**
	 * 考生复试成绩详情页面
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/score/{id}/detail")
	public String studentScoreDetailPage(@PathVariable Long id, Model model){
		model.addAttribute("student", studentRepository.findOne(id));
		model.addAttribute("itemList", itemRepository.findAllWithTeacherByStudentId(id));
		return "student/score/detail";
	}
	
	/**
	 * 修改考生非综合面试成绩表单页面
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/score/{id}/form")
	public String studentScoreUpdateForm(@PathVariable Long id, Model model){
		model.addAttribute("student", studentRepository.findOne(id));
		String api = "/students/score/" + id + "/update";
		model.addAttribute("api", api);
		return "student/score/form";
	}
	
	/**
	 * 修改考生非综合面试成绩
	 * @param id
	 * @param student
	 * @return
	 */
	@PostMapping("/score/{id}/update")
	public String updateStudentScore(@PathVariable Long id, Student student){
		Student os = studentRepository.findOne(id);
		if(os != null){
			os.setProClaScore(student.getProClaScore());
			os.setEngWirScore(student.getEngWirScore());
			os.setEngSpeScore(student.getEngSpeScore());
			//每次修改后触发更新成绩（是否有统一的方式，不用每次修改后更新？）
			ScoreUtil.CalculateOne(os, false);
			studentRepository.flush();
		}
		return "redirect:/students/score/" + id + "/detail";
	}
	
	/**
	 * 自动计算所有成绩
	 * @return
	 */
	@PostMapping("/score/generateAll")
	public ResponseEntity<?> generateAllStudentScore(){
		List<Student> students = studentRepository.findAllWithItems();
		students.stream().forEach(ScoreUtil::CalculateOne);
		studentRepository.flush();
		return ResponseEntity.ok().build();
	}
	
}
