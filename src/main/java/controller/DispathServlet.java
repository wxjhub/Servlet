package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/*")
public class DispathServlet extends HttpServlet {
    Map<String, Method> map = new HashMap<String, Method>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("2222222");
        String path = req.getRequestURI();
        String url = path.substring(path.lastIndexOf("/"));
        Method method = map.get(url);
        Class c = method.getDeclaringClass();
        Object o;
        try {
            o = c.newInstance();
            Object result =method.invoke(o);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }

    @Override
    public void init() throws ServletException {
        System.out.println("1111");
        String l = "controller";
        String root = this.getClass().getResource("/").getPath();
        root+="\\controller";
        System.out.println(root);
        File file  = new File(root);
        File[] files = file.listFiles();
        for (File f:files) {
            try {
                Class c = Class.forName("controller."+f.getName().replace(".class",""));
                Method[] declaredMethods = c.getDeclaredMethods();
                for (Method method:declaredMethods) {
                    if(method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                        String key = requestMapping.value();
                        map.put(key,method);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
