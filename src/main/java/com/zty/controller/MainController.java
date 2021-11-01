package com.zty.controller;


import com.alibaba.fastjson.JSONObject;
import com.zty.Dao.*;
import com.zty.Util.FileUtil;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@NoArgsConstructor
@RestController
public class MainController
{
    private  final String URL = "http://localhost:8081/";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private StudentRepository studentRepository;

    @RequestMapping("/api/login")//登录验证
    public String validUser(@RequestBody JSONObject param) {
        String username=param.getString("username");
        String password=param.getString("password");
        MyUser user = userRepository.findByUsername(username);
        if (user == null)
        {
            return "username";
        }
        else if (password.equals(user.getPassword()))
        {
            return "ok";
        }
        else
        {
            return "password";
        }
    }
    @RequestMapping("/api/firstlogin")//登录验证
    public String firstlogin(@RequestBody JSONObject param) {
        String username=param.getString("username");
        String password=param.getString("password");
        MyUser user = userRepository.findByUsername(username);
        if (user == null)
        {
            return "username";
        }
        else if (password.equals(user.getPassword()))
        {
            user.setCount(user.getCount() + 1);
            user.setLasttime(new Date(System.currentTimeMillis()) + " " + new Time(System.currentTimeMillis()));
            userRepository.save(user);
            System.out.println(user.getLasttime());
            return "ok";
        }
        else
        {
            return "password";
        }
    }

    @RequestMapping("/api/register")//注册用户
    public String register(@RequestBody JSONObject param) {
        String username=param.getString("username");
        String password=param.getString("password");
        MyUser user = userRepository.findByUsername(username);
        if (user == null&& password!=null)
        {
            user = new MyUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setLasttime(new Date(System.currentTimeMillis()) + " " + new Time(System.currentTimeMillis()));
            user.setCount(1);
            user.setAuthority("user");
            user.setAvatar("E:\\note\\"+username+".jpg");
            userRepository.save(user);
            return "ok";
        }
        else if (user == null&& password==null){
            return "ok";
        }
        else
        {
            return "username";
        }
    }

    @RequestMapping(value = "/upload")
    public String uploadFile(@RequestParam MultipartFile file,@RequestParam String username) throws IOException {
        // 文件名
        System.out.println("1"+username);
        String filename=username+ ".jpg";
        String RawName = file.getOriginalFilename();
        // 在file文件夹中创建名为fileName的文件
        assert RawName!= null;
        int split = RawName.lastIndexOf(".");
        String totalFileName="E:\\note\\"+filename;
        // 文件后缀，用于判断上传的文件是否是合法的
        String suffix = RawName.substring(split+1,RawName.length());
        //判断文件类型，因为我这边是图片，所以只设置三种合法格式
        String url = "";
        if("jpg".equals(suffix) || "jpeg".equals(suffix)||"png".equals(suffix)) {
            // 正确的类型，保存文件
            try {
                File distFile = new File(totalFileName);
                if (!distFile.getParentFile().exists()) distFile.getParentFile().mkdirs();
                File savedFile = new File(totalFileName);
                file.transferTo(savedFile);
                url = savedFile.getAbsolutePath();
                System.out.println("图片上传完毕，存储地址为："+ url);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }else {
        }
        return url;
    }

    @RequestMapping("/avatar")
    public void avatar(HttpServletResponse response,@RequestBody JSONObject param) throws Exception {
        response.setHeader("Content-Disposition", "attachment;filename=" + param.getString("username") + ".jpg");
        // 响应类型,编码
        response.setContentType("application/octet-stream;charset=UTF-8");
        // 形成输出流
        OutputStream osOut = response.getOutputStream();
        System.out.println(param);
        String avatarPath=userRepository.findByUsername(param.getString("username")).getAvatar();
        File img = new File(avatarPath);
        InputStream input = null;
        try
        {
            input = new FileInputStream(img);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0)
            {
                osOut.write(buf, 0, bytesRead);
            }
        } finally
        {
            input.close();
            osOut.close();
        }
    }

    @RequestMapping("/saveMd")
    public String saveMd(@RequestBody JSONObject param){
        Blog blog=new Blog();
        blog.setCreated(new Date(System.currentTimeMillis()) + " " + new Time(System.currentTimeMillis()));
        //取出java中对应参数的值
        String str = param.getString("content");
        String name=param.getString("username");
        String title=param.getString("title");
        String tag=param.getString("tag");
        tag=tag.replace("[","");
        tag=tag.replace("]","");
        String root="E:\\note\\"+name+"\\";
        //文件保存路径  F:\note\555.md
        String filepath=root+title+".md";
        blog.setDictionary(filepath);
        blog.setTitle(title);
        blog.setUsername(name);
        blog.setTag(tag);
        blog.setOpen(param.getString("open"));
        blogRepository.save(blog);
        FileUtil.string2File(str,filepath);
        return "ok";
    }

    @RequestMapping("/api/List")
    public List<Blog> List(@RequestParam String username){
        List<Blog> allblog=blogRepository.findAll();
        List<Blog> blogList=blogRepository.findAllByUsername(username);
        for (Blog blog:blogList)
        {
            blog.setDictionary("");
        }
        for (Blog blog:allblog)
        {
            blog.setDictionary("");
        }
        return allblog;
    }
    @RequestMapping("/api/blog")
    public void blogDetail(HttpServletResponse response,@RequestParam String id) throws Exception {
        response.setHeader("Content-Disposition", "attachment;filename=" + id + ".md");
        // 响应类型,编码
        response.setContentType("application/octet-stream;charset=UTF-8");
        // 形成输出流
        OutputStream osOut = response.getOutputStream();
        int ID=Integer.parseInt(id);
        String avatarPath=blogRepository.findById(ID).getDictionary();
        File img = new File(avatarPath);
        InputStream input = null;
        try
        {
            input = new FileInputStream(img);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0)
            {
                osOut.write(buf, 0, bytesRead);
            }
        } finally
        {
            input.close();
            osOut.close();
        }
    }
    @RequestMapping("/api/mdImg")
    public String mdImg(@RequestParam MultipartFile image){
        // 文件名
        //获取项目classes/static的地址
        String path = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        String fileName = image.getOriginalFilename();  //获取文件名
        //图片访问URI(即除了协议、地址和端口号的URL)
        String url_path = "image/"+fileName;
        System.out.println("图片访问uri："+url_path);
        String savePath = path+"/"+url_path;  //图片保存路径
        System.out.println("图片保存地址："+savePath);
        File saveFile = new File(savePath);
        if (!saveFile.exists()){
            saveFile.mkdirs();
        }
        try {
            image.transferTo(saveFile);  //将临时存储的文件移动到真实存储路径下
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回图片访问地址
        System.out.println("访问URL："+URL+url_path);
        return URL+url_path;
    }
    @RequestMapping("/api/signIn")
    public String signIn(@RequestBody JSONObject param){
        System.out.println(param);
        Student student=new Student();
        String name=param.getString("name");
        List<Student> students= studentRepository.findAllByNameAndLeaveTimeIsNull(name);
        if (students.size()!=0){
            return "error";
        }
        student.setName(param.getString("name"));
        student.setId(param.getString("id"));
        student.setArriveTime(new Date(System.currentTimeMillis()) + " " + new Time(System.currentTimeMillis()));
        studentRepository.save(student);
        return "ok";
    }
    @RequestMapping("/api/studentList")
    public List<Student> studentList(){
        return studentRepository.findAllByLeaveTimeIsNull();
    }
    @RequestMapping("/api/logout")
    public String logout(@RequestParam String name){
        System.out.println(name);
        List<Student> students= studentRepository.findAllByNameAndLeaveTimeIsNull(name);
        System.out.println(students.size());
        System.out.println(students);
        if (students.isEmpty()){
            return "none";
        }
        else if(students.size()>1){
            return "error";
        }
        else
        {
            Student student=students.get(0);
            student.setLeaveTime(new Date(System.currentTimeMillis()) + " " + new Time(System.currentTimeMillis()));
            studentRepository.save(student);
            return "ok";
        }

    }
}

