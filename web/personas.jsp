<%@page import="cl.beans.PersonaBean"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="cl.beans.PersonaBeanLocal"%>
<%@include file="template/header.jsp" %>


<c:if test="${not empty sessionScope.admin}">
    <%@include file="template/menu.jsp" %>

    <div class="row">
        <div class="col s6 offset-s3 z-depth-3">
            <h4 class="center-align">Gestion Personas</h4>
            <form action="control.do" method="post">
                <table class="bordered">
                    <thead>
                        <tr>
                            <th>Rut</th>
                            <th>Nombre</th>
                            <th>Mail</th>
                            <th>Perfil</th>
                            <th>Activo</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <%! private PersonaBeanLocal beanPersona;%>
                        <%
                            InitialContext ctx = new InitialContext();
                            beanPersona = (PersonaBeanLocal) ctx.lookup("java:global/EjercicioMartes/PersonaBean!cl.beans.PersonaBeanLocal");
                        %>
                        <c:set var="lista" scope="page" value="<%= beanPersona.getPersonaList() %>" />
                        <c:forEach items="${pageScope.lista}" var="p">
                            <tr>
                                <td>${p.rut}</td>
                                <td>${p.nombre}</td>
                                <td>${p.mail}</td>
                                <td>${p.perfil}</td>
                                <td>${p.activo?"Si":"No"}</td>
                                <td>
                                    <button name="boton" value="${p.rut}"
                                            class="btn-floating blue">
                                        <i class="material-icons">
                                            edit
                                        </i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </form>
        </div>
    </div>

</c:if>

<c:if test="${empty sessionScope.admin}">
    No tienes permisos para estar aqui.
    <br> <a href="index.jsp">Iniciar Session</a>
</c:if>    


<%@include file="template/footer.jsp" %>