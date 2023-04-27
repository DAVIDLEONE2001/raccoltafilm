package it.prova.raccoltafilm.web.servlet.regista;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.prova.raccoltafilm.model.Regista;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RegistaService;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteEditRegistaServlet
 */
@WebServlet("/ExecuteEditRegistaServlet")
public class ExecuteEditRegistaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private RegistaService registaService;

	public ExecuteEditRegistaServlet() {
		this.registaService = MyServiceFactory.getRegistaServiceInstance();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String nomeParam = request.getParameter("nome"); 
		String cognomeParam = request.getParameter("cognome"); 
		String nickNameParam = request.getParameter("nickName"); 
		String dataParam = request.getParameter("dataDiNascita");
		String sessoParam = request.getParameter("sesso");
		String idParam = request.getParameter("idRegista");

		Regista registaTemp = UtilityForm.createRegistaFromParams(nomeParam,cognomeParam,nickNameParam,dataParam,sessoParam);

		if (!UtilityForm.validateRegistaBean(registaTemp)){
			request.setAttribute("show_regista_attr", registaTemp);
			request.setAttribute("errorMessage", "Attenzione sono presenti errori di inserimento");
			request.getRequestDispatcher("/regista/edit.jsp").forward(request, response);
			return;
		}
		
		try {
			
			registaTemp.setId(Long.parseLong(idParam));
			registaService.aggiorna(registaTemp);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/regista/insert.jsp").forward(request, response);
			return;
		}

		// andiamo ai risultati
		// uso il sendRedirect con parametro per evitare il problema del double save on
		// refresh
		response.sendRedirect("ExecuteListRegistaServlet?operationResult=SUCCESS");
		
	}

}
