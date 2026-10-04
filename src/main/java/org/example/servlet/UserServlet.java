package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.service.UserService;

import java.io.IOException;
import java.util.List;


//we learnt that here tomcat only handle this class likewise SpringIOC manages the beans
//and the tomcat server recognises this class to handle is annotated by @WebServlet
@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();//here we are making user service class manually because here there is no spring container


//    we also learnt that tomcat is acting as a servlet container and this UserServlet is like a servlet so
//    in the servlet container it has HttpRequest and HttpResponse object and it passes to multiple
//    servelets according to end points and there servlets will check the request and set data in response


    //    Here u are seeing all methods return type is void because we dont have to return anything we simply have to get the values from request and
//    have to set the values in response object. afterwards tomcat server will handle
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        if (id == null) {
            List<User> users = userService.getAllUsers();
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().write(usersToJson(users));
            return;
//            return users;
        }
        ;
        User userResp = userService.getById(id);
        if(userResp == null) {
            response.setStatus(404);
            response.setContentType("application/json");
        }

        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().write(userToJson(userResp));
        super.doGet(req, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        if (id == null || email == null ||
                name == null) {
            response.setStatus(400);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\n" +
                            "    \"message\" : \"Some fields are missing\"\n" +
                            "}"
            );
        }
        User user = new User(id, name, email);
        User createdUser = userService.createUser(user);
        response.setStatus(201);
        response.setContentType("application/json");
        response.getWriter().write(
                "{\n" +
                        "    \"message\" : \"User Added successfully\"\n" +
                        "}"
        );
//        super.doPost(req, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    private String userToJson(User user) {
        return "{\n" +
                "    \"id\" : " + user.getId() + ",\n" +
                "    \"name\" : " + user.getName() + ",\n" +
                "    \"email\" : " + user.getEmail() + ",\n" +
                "}";
    }

    private String usersToJson(List<User> users) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for(int i = 0; i<users.size(); i++) {
            stringBuilder.append(userToJson(users.get(i)));

            if(i < users.size() - 1) {
                stringBuilder.append(",");
            }
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}

//LIFECYCLE OF TOMCAT SERVER
//1.init() like in postConstruct in spring IOC after servlet creation
//2.service()  doGet() doPost() ...
//3.destroy() like preDestroy() in spring IOC

//GET --> /hello  ---> TOMCAT --> lazily created(when needed ) HelloServlet
//
//    constructor()
//    init() if servlet overrided init method
//    service() for each request but not neccesary to override because it calls same doget and doPost which we are already doing override
//
//        HelloServlet hs = new HelloServlet();
//        (constructor ) initialised.
//        hs.init()
//        hs.service(req,res) ---> calls doGet ...
//        destroy()




//DISPATCHER SERVLET

//instead of sending or mapping different servlet for different route bt TOMCAT
//we can make a central servlet -> tomcat will forward route to it and that central servlet will route to dofferent servlets

//that central servelet is called DISPATCHER SERVLET
// here we can log validate incoming request and outgoing response