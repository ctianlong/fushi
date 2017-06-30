package springboot.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import springboot.config.Constants;
import springboot.domain.CompInteScoItem;
import springboot.domain.Role;
import springboot.domain.Student;
import springboot.domain.User;
import springboot.errors.Message;
import springboot.repository.CompInteScoItemRepository;
import springboot.repository.RoleRepository;
import springboot.repository.StudentRepository;
import springboot.repository.UserRepository;
import springboot.rest.vm.UserVM;
import springboot.security.RoleConstants;
import springboot.security.SecurityUser;
import springboot.security.SecurityUtil;
import springboot.util.ScoreUtil;

@Controller
@RequestMapping("/users")
public class UserController {
	
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;
	
	private final StudentRepository studentRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final CompInteScoItemRepository itemRepository;
	
	public UserController(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder, CompInteScoItemRepository itemRepository, StudentRepository studentRepository){
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.itemRepository = itemRepository;
		this.studentRepository = studentRepository;
	}
	
	@GetMapping
	public String userList(Model model){
		model.addAttribute("list", userRepository.findAllWithRoles());
		return "user/list";
	}
	
	@GetMapping("/form")
	public String userAddForm(Model model){
		model.addAttribute("roleList", roleRepository.findAll());
		//model.addAttribute("roleWithGroup", roleRepository.findOne(RoleConstants.USER));
		model.addAttribute("api", "/users/add");
		return "user/form";
	}
	
	@PostMapping("/add")
	public String addUser(User user, @RequestParam(name = "rname", required = false) HashSet<String> rname){
		if (StringUtils.isNotBlank(user.getUsername()) && StringUtils.isNotBlank(user.getFullname())) {
			if (rname != null) {
				user.setRoles(rname.stream().map(roleRepository::findOne).collect(Collectors.toSet()));
				if (rname.contains(RoleConstants.USER)) {
					user.setAgreed(false);
					user.setSubmitted(false);
				}
			}
			user.setPassword(passwordEncoder.encode(Constants.INITIAL_PASSWORD));
			userRepository.saveAndFlush(user);
		}
		return "redirect:/users";
	}
	
	@GetMapping("/{id}/form")
	public String userUpdateForm(@PathVariable Long id, Model model){
		model.addAttribute("user", userRepository.findOneWithRolesById(id));
		model.addAttribute("roleList", roleRepository.findAll());
		//model.addAttribute("roleWithGroup", roleRepository.findOne(RoleConstants.USER));
		model.addAttribute("api", "/users/" + id + "/update");
		return "user/form";
	}
	
	@PostMapping("/{id}/update")
	public String updateUser(@PathVariable Long id, User user, @RequestParam(name = "rname", required = false) HashSet<String> rname){
		User ou = userRepository.findOneWithRolesById(id);
		if (ou != null && StringUtils.isNotBlank(user.getUsername()) && StringUtils.isNotBlank(user.getFullname())) {
			ou.setUsername(user.getUsername());
			ou.setFullname(user.getFullname());
			Set<Role> roles = ou.getRoles();
			if (rname != null) {
				if (roles.contains(RoleConstants.USER)) {
					if (!rname.contains(RoleConstants.USER)) {
						ou.setAgreed(null);
						ou.setSubmitted(null);
						ou.setGroupNo(null);
					}
				} else if (rname.contains(RoleConstants.USER)) {
					ou.setAgreed(false);
					ou.setSubmitted(false);
				}
				roles.clear();
				rname.stream().map(roleRepository::findOne).forEach(roles::add);
			}
			userRepository.flush();
		}
		return "redirect:/users";
	}

	@DeleteMapping("/{id}/delete")
	@ResponseBody
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		try {
			List<CompInteScoItem> deleteItems = itemRepository.findAllByTeacherId(id);
			itemRepository.delete(deleteItems);
			User teacher = userRepository.findOne(id);
			List<Student> students = studentRepository.findAllWithItemsByGroupNo(teacher.getGroupNo());
			students.parallelStream().forEach(ScoreUtil::CalculateOne);
			studentRepository.flush();
			userRepository.delete(teacher);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().header("X-app-error", "user.delete.error")
					.header("X-app-params", id.toString()).build();
		}
		return ResponseEntity.ok().header("X-app-alert", "user.delete.success")
				.header("X-app-params", id.toString()).build();
	}
	
	/**
	 * 修改当前登录用户密码表单
	 * @return
	 */
	@GetMapping("/password/form")
	public String changePasswordForm(Model model) {
		model.addAttribute("api", "/users/password/change");
		return "user/passwordForm";
	}
	
	@PostMapping("/password/change")
	@ResponseBody
	public ResponseEntity<?> changePassword(String password) {
		if (checkPasswordLength(password)) {
			userRepository.findOneByUsername(SecurityUtil.getCurrentUsername()).ifPresent(user -> {
				user.setPassword(passwordEncoder.encode(password));
			});
			userRepository.flush();
		}
		return ResponseEntity.ok().build();
	}
	
	private boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= UserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= UserVM.PASSWORD_MAX_LENGTH;
    }
	
	@PostMapping("/teacher/agree")
	@ResponseBody
	public ResponseEntity<?> agreeForTeacher() {
		userRepository.findOneById(SecurityUtil.getCurrentUid()).ifPresent(user -> {
			user.setAgreed(true);
			userRepository.flush();
			SecurityUser su = SecurityUtil.getCurrentUser();
			if (su != null) {
				su.setAgreed(true);
			}
		});
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/teacher/submit")
	@ResponseBody
	public ResponseEntity<?> submitForTeacher () {
		List<CompInteScoItem> items = itemRepository.findAllByTeacherId(SecurityUtil.getCurrentUid());
		if (items.size() > 0 && items.stream().allMatch(item -> item.getCompInteScoSum() != null)) {
			userRepository.findOneById(SecurityUtil.getCurrentUid()).ifPresent(user -> {
				user.setSubmitted(true);
				userRepository.flush();
				SecurityUser su = SecurityUtil.getCurrentUser();
				if (su != null) {
					su.setSubmitted(true);
				}
			});
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/teachers")
	public String teacherList(Model model) {
		model.addAttribute("list", userRepository.findAllByRolesName(RoleConstants.USER));
		// 使用Specification
//		model.addAttribute("list", userRepository.findAll(new Specification<User>() {
//			@Override
//			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//				Join<User, Role> join = root.join(root.getModel().getSet("roles", Role.class), JoinType.LEFT);
//				//可加可不加，因为即使多对多，但指定角色名称时所查询出的每个用户只有一条记录
//				query.distinct(true);
//				
//				//也可以如下形式
//				/*query.where(cb.equal(join.<String>get("name"), RoleConstants.USER));
//				return null;*/
//				
//				return cb.equal(join.<String>get("name"), RoleConstants.USER);
//			}
//		}));
		return "user/teacherList";
	}
	
	@PostMapping("/teachers/{id}/changeGroupNo")
	@ResponseBody
	public ResponseEntity<?> changeGroupNo(@PathVariable Long id, @RequestBody User user){
		User os = userRepository.findOne(id);
		if (os != null) {
			os.setGroupNo(user.getGroupNo());
			userRepository.flush();
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}
	
	/**
	 * 教师信息Excel模板
	 * @param response
	 * @throws IOException 
	 */
	@GetMapping("/teachers/excelTemplate")
	public void teacherInfoExcelTemplate(HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("教师基本信息");
			XSSFRow row = sheet.createRow(0);
			row.createCell(0).setCellValue("用户名");
			row.createCell(1).setCellValue("姓名");
			row.createCell(2).setCellValue("组号");
			
			row = sheet.createRow(1);
			row.createCell(0).setCellValue("10000");
			row.createCell(1).setCellValue("张老师");
			row.createCell(2, CellType.NUMERIC).setCellValue(1);
			
			String fileName = URLEncoder.encode("教师基本信息模板.xlsx", "UTF-8");
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
	 * 导入教师基本信息
	 * @param file
	 * @return
	 */
	@PostMapping("/teachers/importExcel")
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
		
		List<User> us = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		try {
			Sheet sheet = workbook.getSheetAt(0);
			sheet.removeRow(sheet.getRow(sheet.getFirstRowNum()));
			Iterator<Row> rowIterator = sheet.rowIterator();
			Role teacherRole = roleRepository.findOne(RoleConstants.USER);
			Row row = null;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				User u = new User();
				us.add(u);
				try {
					int i = row.getFirstCellNum();
					
					Cell cell = row.getCell(i);
					switch (cell.getCellTypeEnum()) {
					case NUMERIC:
						u.setUsername(String.format("%.0f", cell.getNumericCellValue()));
						break;
					case STRING:
						u.setUsername(cell.getStringCellValue());
						break;
					default:
						break;
					}
					
					u.setFullname(row.getCell(i + 1).getStringCellValue());
					
					cell = row.getCell(i + 2);
					if (cell != null) {
						switch (cell.getCellTypeEnum()) {
						case NUMERIC:
							u.setGroupNo((byte) cell.getNumericCellValue());
							break;
						case STRING:
							u.setGroupNo(Byte.parseByte(cell.getStringCellValue()));
							break;
						default: //若要当单元格既不是数字，又不是字符串时报错的话，可在此处抛异常
							break;
						}
					}
					
					u.setPassword(passwordEncoder.encode(Constants.INITIAL_PASSWORD));
					u.getRoles().add(teacherRole);
					u.setAgreed(false);
					u.setSubmitted(false);
					
				} catch (Exception e) {
					sb.append(row.getRowNum() + 1).append(",");
					us.remove(us.size() - 1);
				}
			}
			userRepository.save(us);
			userRepository.flush();
			String note;
			if (sb.length() > 0) {
				note = "共导入 " + us.size() + " 条记录\n" + "以下记录导入失败(Excel行号)：" + sb.deleteCharAt(sb.length() - 1).toString();
			} else {
				note = "全部导入成功\n" + "共导入 " + us.size() + " 条记录";
			}
			Message message = new Message(true);
			message.setData(note);
			return ResponseEntity.ok().body(message);
		}catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			Message message = new Message(false);
			message.setErrorMessage("导入失败，存在教师用户名重复！");
			return ResponseEntity.badRequest().body(message);
		}catch (Exception e) {
			e.printStackTrace();
			Message message = new Message(false);
			message.setErrorMessage("导入失败！");
			return ResponseEntity.badRequest().body(message);
		}
		
	}
	
	
}
