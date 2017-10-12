package springboot.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import springboot.domain.CompInteScoItem;
import springboot.domain.Student;
import springboot.errors.Message;
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
			List<CompInteScoItem> deleteItems = itemRepository.findAllByStudentId(id);
			itemRepository.delete(deleteItems);
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
	@ResponseBody
	public ResponseEntity<?> generateAllStudentScore(){
		List<Student> students = studentRepository.findAllWithItems();
		students.parallelStream().forEach(ScoreUtil::CalculateOne);
		studentRepository.flush();
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 导出考生最终复试成绩
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/score/exportExcel")
	public void exportScoreExcel(HttpServletResponse response) throws IOException {
		List<Student> students = studentRepository.findAll();
		OutputStream outputStream = null;
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("考生复试成绩");
			XSSFRow row = sheet.createRow(0);
			row.createCell(0).setCellValue("姓名");
			row.createCell(1).setCellValue("组号");
			row.createCell(2).setCellValue("本科院校");
			row.createCell(3).setCellValue("联系方式");
			row.createCell(4).setCellValue("家庭住址");
			row.createCell(5).setCellValue("第一导师");
			row.createCell(6).setCellValue("第二导师");
			row.createCell(7).setCellValue("专业课笔试");
			row.createCell(8).setCellValue("英语笔试");
			row.createCell(9).setCellValue("英语口语");
			row.createCell(10).setCellValue("本科成绩及科研业绩");
			row.createCell(11).setCellValue("专业基础能力");
			row.createCell(12).setCellValue("培养潜力");
			row.createCell(13).setCellValue("分析和语言能力");
			row.createCell(14).setCellValue("综合素质");
			row.createCell(15).setCellValue("合计");
			row.createCell(16).setCellValue("原始总分");
			row.createCell(17).setCellValue("折算总分");
			
			int StuNum = students.size();
			for (int i = 0; i < StuNum; i++){
				Student s = students.get(i);
				row = sheet.createRow(i + 1);
				if (s.getFullname() != null) {
					row.createCell(0).setCellValue(s.getFullname());
				}
				if (s.getGroupNo() != null) {
					row.createCell(1).setCellValue(s.getGroupNo());
				}
				if (s.getGraduatedCollege() != null) {
					row.createCell(2).setCellValue(s.getGraduatedCollege());
				}
				if (s.getTelephone() != null) {
					row.createCell(3).setCellValue(s.getTelephone());
				}
				if (s.getHomeAddress() != null) {
					row.createCell(4).setCellValue(s.getHomeAddress());
				}
				if (s.getFirstTutor() != null) {
					row.createCell(5).setCellValue(s.getFirstTutor());
				}
				if (s.getSecondTutor() != null) {
					row.createCell(6).setCellValue(s.getSecondTutor());
				}
				if (s.getProClaScore() != null) {
					row.createCell(7).setCellValue(s.getProClaScore());
				}
				if (s.getEngWirScore() != null) {
					row.createCell(8).setCellValue(s.getEngWirScore());
				}
				if (s.getEngSpeScore() != null) {
					row.createCell(9).setCellValue(s.getEngSpeScore());
				}
				if (s.getCompInteSco1() != null) {
					row.createCell(10).setCellValue(s.getCompInteSco1().doubleValue());
				}
				if (s.getCompInteSco2() != null) {
					row.createCell(11).setCellValue(s.getCompInteSco2().doubleValue());
				}
				if (s.getCompInteSco3() != null) {
					row.createCell(12).setCellValue(s.getCompInteSco3().doubleValue());
				}
				if (s.getCompInteSco4() != null) {
					row.createCell(13).setCellValue(s.getCompInteSco4().doubleValue());
				}
				if (s.getCompInteSco5() != null) {
					row.createCell(14).setCellValue(s.getCompInteSco5().doubleValue());
				}
				if (s.getCompInteTotScore() != null) {
					row.createCell(15).setCellValue(s.getCompInteTotScore().doubleValue());
				}
				if (s.getOriTotScore() != null) {
					row.createCell(16).setCellValue(s.getOriTotScore().doubleValue());
				}
				if (s.getLasTotScore() != null) {
					row.createCell(17).setCellValue(s.getLasTotScore().doubleValue());
				}
			}
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		    String fileName = URLEncoder.encode("考生复试成绩" + LocalDateTime.now().format(dtf) + ".xlsx", "UTF-8");
		    response.reset();
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
		    response.setContentType("application/octet-stream; charset=UTF-8");
		    outputStream = new BufferedOutputStream(response.getOutputStream());
		    workbook.write(outputStream);	    
		    outputStream.flush();
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (workbook != null) {
				workbook.close();
			}
		}
	}
	
	
	/**
	 * 导入考生基本信息
	 * @param file
	 * @return
	 */
	@PostMapping("/info/importExcel")
	@ResponseBody
	public ResponseEntity<Message> importStudentInfoExcel(MultipartFile file/*, RedirectAttributes redirectAttributes*/) {
		if (file == null || file.isEmpty()) {
			Message message = new Message(false);
			message.setErrorMessage("文件不能为空！");
			return ResponseEntity.badRequest().body(message);
		}
		Workbook workbook = null;
		String fileName = file.getOriginalFilename();
		if (fileName.endsWith(".xlsx")) {
			try {
				workbook = new XSSFWorkbook(file.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (fileName.endsWith(".xls")) {
			try {
				workbook = new HSSFWorkbook(file.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Message message = new Message(false);
			message.setErrorMessage("文件格式错误！");
			return ResponseEntity.badRequest().body(message);
		}
		
		List<Student> ss = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		try {
			Sheet sheet = workbook.getSheetAt(0);
			sheet.removeRow(sheet.getRow(sheet.getFirstRowNum()));
			Iterator<Row> rowIterator = sheet.rowIterator();
			Row row = null;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Student s = new Student();
				ss.add(s);
				try {
					int i = row.getFirstCellNum();
					
					Cell cell = row.getCell(i);
					if (cell != null) {
						switch (cell.getCellTypeEnum()) {
						case NUMERIC:
							s.setGroupNo((byte) cell.getNumericCellValue());
							break;
						case STRING:
							s.setGroupNo(Byte.parseByte(cell.getStringCellValue()));
							break;
						default: //若要当单元格既不是数字，又不是字符串时报错的话，可在此处抛异常
							break;
						}
					}
					
					s.setFullname(row.getCell(i + 1).getStringCellValue());
					
					cell = row.getCell(i + 2);
					if (cell != null) {
						s.setGraduatedCollege(cell.getStringCellValue());
					}
					
					cell = row.getCell(i + 3);
					if (cell != null) {
						s.setFirstTutor(cell.getStringCellValue());
					}
					
					cell = row.getCell(i + 4);
					if (cell != null) {
						s.setSecondTutor(cell.getStringCellValue());
					}
					
					cell = row.getCell(i + 5);
					if (cell != null) {
						switch (cell.getCellTypeEnum()) {
						case NUMERIC:
							s.setTelephone(String.format("%.0f", cell.getNumericCellValue()));
							break;
						case STRING:
							s.setTelephone(cell.getStringCellValue());
							break;
						default:
							break;
						}
					}
					
					cell = row.getCell(i + 6);
					if (cell != null) {
						s.setHomeAddress(cell.getStringCellValue());
					}
				} catch (Exception e) {
					sb.append(row.getRowNum() + 1).append(",");
					ss.remove(ss.size() - 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message message = new Message(false);
			message.setErrorMessage("导入失败！");
			return ResponseEntity.badRequest().body(message);
		}
		
		studentRepository.save(ss);
		studentRepository.flush();
		String note;
		if (sb.length() > 0) {
			note = "共导入 " + ss.size() + " 条记录\n" + "以下记录导入失败(Excel行号)：" + sb.deleteCharAt(sb.length() - 1).toString();
		} else {
			note = "全部导入成功\n" + "共导入 " + ss.size() + " 条记录";
		}
		Message message = new Message(true);
		message.setData(note);
		return ResponseEntity.ok().body(message);
	}
	
	
	/**
	 * 考生基本信息模板
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/info/excelTemplate")
	public void stuInfoExcelTemplate(HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("考生基本信息");
			XSSFRow row = sheet.createRow(0);
			row.createCell(0).setCellValue("组号");
			row.createCell(1).setCellValue("姓名");
			row.createCell(2).setCellValue("本科院校");
			row.createCell(3).setCellValue("第一导师");
			row.createCell(4).setCellValue("第二导师");
			row.createCell(5).setCellValue("联系方式");
			row.createCell(6).setCellValue("家庭住址");
			
			row = sheet.createRow(1);
			row.createCell(0, CellType.NUMERIC).setCellValue(1);
			row.createCell(1).setCellValue("张三");
			row.createCell(2).setCellValue("同济大学");
			row.createCell(3).setCellValue("王老师");
			row.createCell(4).setCellValue("马老师");
			row.createCell(5).setCellValue("18800001111");
			row.createCell(6).setCellValue("上海市嘉定区");
			
			String fileName = URLEncoder.encode("考生基本信息模板.xlsx", "UTF-8");
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		    response.setContentType("application/octet-stream; charset=UTF-8");
		    outputStream = new BufferedOutputStream(response.getOutputStream());
		    workbook.write(outputStream);	    
		    outputStream.flush();
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (workbook != null) {
				workbook.close();
			}
		}
	}
	
	
}
