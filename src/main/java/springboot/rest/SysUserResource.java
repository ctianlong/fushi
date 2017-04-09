package springboot.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springboot.domain.SysRole;
import springboot.domain.SysUser;
import springboot.repository.SysRoleRepository;
import springboot.repository.SysUserRepository;

/**
 * 手动编写 Rest API，可自定义操作
 * 一般也可通过 Spring Data REST 将 Repository 自动输出为 REST
 * @author Tianlong
 *
 */
@RestController
@RequestMapping("/api/user")
public class SysUserResource {

	@Autowired
	private SysUserRepository sysUserRepository;
	@Autowired
	private SysRoleRepository sysRoleRepository;
	
	//注入EntityManager,两种均可
//	@PersistenceContext
	@Autowired
	private EntityManager entityManager;

	@RequestMapping(method = RequestMethod.GET, value = "/sayhello", produces = "text/plain;charset=UTF-8")
	public String sayHello() {
		return "Hello World";
	}

	@GetMapping(value="")
	public List<SysUser> getSysUserList() {
		// 处理"/api/user"的GET请求，用来获取用户列表 
        // 还可以通过@RequestParam从页面中传递参数来进行查询条件或者翻页信息的传递
		return sysUserRepository.findAll();
	}
	
	@GetMapping(value="/{id}")
	public SysUser getSysUser(@PathVariable Long id){
		// 处理"/api/user/{id}"的GET请求，用来获取url中id值的User信息 
        // url中的id可通过@PathVariable绑定到函数的参数中 
		return sysUserRepository.findOne(id);
	}
	
	//此处创建不应该包括创建关联关系
	@PostMapping(value="")
	@Transactional
	public SysUser postSysUser(@RequestBody SysUser sysUser){
		// 处理"/api/user"的POST请求，用来创建User 
        // 除了@ModelAttribute绑定参数之外，还可以通过@RequestParam从页面中传递参数
		
		//作为测试，模拟创建关联关系，实际是不应该的
//		Set<SysRole> temp = new HashSet<>();
//		for(SysRole role : sysUser.getRoles()){
//			temp.add(sysRoleRepository.findOne(role.getId()));  //可对sysRoleRepository.findOne(role.getId())进行缓存减少数据库查询
//		}
//		sysUser.setRoles(temp);
		
//		sysUserRepository.saveAndFlush(sysUser);
		
		
		//能够保存关联关系，但是内部关联对象（SysRole）除了ID（ID属性在保存前传入，用于指明关联关系，其它属性在保存前未传入）以外的其它属性仍为null，并不会自动查询，需要手动refresh将这些属性查询出来
		//默认，flush会清理缓存，进行脏检查
		sysUserRepository.saveAndFlush(sysUser);
		
//		entityManager.refresh(sysUser.getRoles());  //refresh无效，报错Entity not managed
		
//		for(SysRole role : sysUser.getRoles()){
//			entityManager.refresh(role);  //refresh无效，报错Entity not managed
//		}
		
		entityManager.refresh(sysUser);  //refresh有效
		return sysUser;
	}
	
	//完全更新对象，会清除对象间的关联关系，因为save(sysUser)中的sysUser并不包含关联属性
	//此处更新不包括关联关系更新
	@PutMapping(value="/{id}")
	public SysUser putSysUser(@PathVariable Long id, @RequestBody SysUser sysUser){
		//处理"/api/user/{id}"的PUT请求，用来更新User信息
		sysUser.setId(id);
		
		SysRole r = new SysRole();
		r.setName("ROLE_USER");;
		sysUser.getRoles().add(r);
		
		//该save方法为完全更新。
		//先根据ID查询原有对象，与传入对象进行比较。对于基本属性（不包括关联对象或集合属性），若全部相同则不更新这些属性，若存在不相同字段则所有字段均发出更新语句，注意会将NULL值也更新到数据库；
		//对于关联属性，会比较两者差异从而更新相应的外键或关联表。
		return sysUserRepository.save(sysUser);
	}
	
	//部分更新对象，只更新更改的字段，对象间的关联关系仍保持完整（当然可以手动修改关联关系）
	//此处更新不包括关联关系更新
	@PatchMapping(value="/{id}")
	public SysUser patchSysUser(@PathVariable Long id, @RequestBody SysUser sysUser){
		SysUser oldUser = sysUserRepository.findOne(id);
		if(oldUser == null) return null;
		if(sysUser.getUsername() != null) oldUser.setUsername(sysUser.getUsername());
		if(sysUser.getPassword() != null) oldUser.setPassword(sysUser.getPassword());
		
		//手动修改关联关系
//		oldUser.getRoles().clear();
		
		sysUserRepository.flush();
		return oldUser;
	}
	
	@DeleteMapping(value="/{id}")
	public String deleteSysUser(@PathVariable Long id){
		// 处理"/api/user/{id}"的DELETE请求，用来删除User
		sysUserRepository.delete(id);
		return "success";
	}

}
