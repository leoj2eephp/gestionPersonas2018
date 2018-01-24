package cl.controller;

import cl.beans.PersonaBeanLocal;
import cl.model.Persona;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ControladorServlet", urlPatterns = {"/control.do"})
public class ControladorServlet extends HttpServlet {

    @EJB
    private PersonaBeanLocal beanPersona;
    // Definición de instancias para MDB
    @Resource(mappedName = "jms/QueueFactory")
    QueueConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/Queue")
    Queue queue;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String boton = request.getParameter("boton");
        switch (boton) {
            case "login":
                login(request, response);
                break;
            case "registro":
                registro(request, response);
                break;
            case "editar":
                editar(request, response);
                break;

            default:
                procesaRut(request, response, boton);
        }
    }

    protected void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String rut = request.getParameter("rut");
        String activo = request.getParameter("activo");
        boolean active = Boolean.valueOf(activo);

        beanPersona.editar(new Persona(rut, "", "", "", "", active));
        response.sendRedirect("personas.jsp");
    }

    protected void procesaRut(HttpServletRequest request, HttpServletResponse response, String boton)
            throws ServletException, IOException {
        Persona p = beanPersona.buscar(boton);
        request.setAttribute("persona", p);
        request.getRequestDispatcher("editarPersona.jsp").forward(request, response);

    }

    protected void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String rut = request.getParameter("rut");
        String clave = request.getParameter("clave");

        Persona p = beanPersona.loguear(rut, clave);
        if (p == null) {
            request.setAttribute("msg", "Hubo un error al iniciar sesion :(");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else if (p.getPerfil().equalsIgnoreCase("administrador")) {
            request.getSession().setAttribute("admin", p);
            response.sendRedirect("inicio.jsp");
        } else {
            request.getSession().setAttribute("person", p);
            response.sendRedirect("inicio.jsp");
        }
    }

    protected void registro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String rut = request.getParameter("rut");
        String nombre = request.getParameter("nombre");
        String mail = request.getParameter("mail");
        String clave1 = request.getParameter("clave1");
        String clave2 = request.getParameter("clave2");
        String msg = "";
        Persona persona = new Persona(rut, nombre, "Persona", mail, clave1, true);
        if (clave1.equals(clave2)) {
            msg = msg.concat(beanPersona.add(persona));
            try {   // Envío de mensaje por MessageDriven
                Connection conn = connectionFactory.createConnection();
                Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer messageProducer = session.createProducer(queue);
                MapMessage mensaje = session.createMapMessage();
                mensaje.setString("mensaje", "Hola mundo!!");
                messageProducer.send(mensaje);
                messageProducer.close();
                session.close();
                conn.close();
            } catch (JMSException ex) {
                ex.printStackTrace();
            }

            request.getRequestDispatcher("registro.jsp").forward(request, response);
        } else {
            msg = msg.concat("Claves no coinciden..");
            request.setAttribute("msg", msg);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
