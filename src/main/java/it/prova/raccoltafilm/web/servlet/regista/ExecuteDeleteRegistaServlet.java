package it.prova.raccoltafilm.web.servlet.regista;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import it.prova.raccoltafilm.exception.RegistaConFilmException;
import it.prova.raccoltafilm.exceptions.ElementNotFoundException;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RegistaService;

/**
 * Servlet implementation class ExecuteDeliteRegistaServlet
 */
@WebServlet("/admin/ExecuteDeleteRegistaServlet")
public class ExecuteDeleteRegistaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private RegistaService registaService;
	
    public ExecuteDeleteRegistaServlet() {
        this.registaService= MyServiceFactory.getRegistaServiceInstance();
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idRegistaParam = request.getParameter("idRegista");

		if (!NumberUtils.isCreatable(idRegistaParam)) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			response.sendRedirect(request.getContextPath() + "/home?operationResult=ERROR");
			return;
		}
		
		try {
			if (!registaService.caricaSingoloElementoConFilms(Long.parseLong(idRegistaParam)).getFilms().isEmpty()){
			
				throw new RegistaConFilmException("Errore");
				
			}
		} catch (RegistaConFilmException e) {
			request.setAttribute("errorMessage", "Errore: Regista Con Film");
			request.getRequestDispatcher("/ExecuteListRegistaServlet").forward(request, response);
			return;
		} catch (Exception e) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/home?operationResult=ERROR");
			return;
		}

		try {
			
			registaService.rimuovi(Long.parseLong(idRegistaParam));
		} catch (ElementNotFoundException e) {
			response.sendRedirect(request.getContextPath() + "/ExecuteListRegistaServlet?operationResult=NOT_FOUND");
			return;
		} catch (Exception e) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/home?operationResult=ERROR");
			return;
		}

		response.sendRedirect(request.getContextPath() +"/ExecuteListRegistaServlet?operationResult=SUCCESS");
	}

}
