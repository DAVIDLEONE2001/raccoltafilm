package it.prova.raccoltafilm.web.servlet.film;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import it.prova.raccoltafilm.model.Film;
import it.prova.raccoltafilm.service.FilmService;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteEditFilmServlet
 */
@WebServlet("/ExecuteEditFilmServlet")
public class ExecuteEditFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	FilmService filmServiceIstance;

	public ExecuteEditFilmServlet() {

		this.filmServiceIstance = MyServiceFactory.getFilmServiceInstance();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String titoloParams = request.getParameter("titolo");
		String genereParams = request.getParameter("genere");
		String dataParams = request.getParameter("dataPubblicazione");
		String minutiParams = request.getParameter("minutiDurata");
		String idRegistaParams = request.getParameter("regista.id");
		String idfilmParams = request.getParameter("idFilm");

		Film filmInstance = UtilityForm.createFilmFromParams(titoloParams, genereParams, minutiParams, dataParams,
				idRegistaParams);

		if (!UtilityForm.validateFilmBean(filmInstance)) {
			request.setAttribute("insert_film_attr", filmInstance);
			request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
			request.getRequestDispatcher("/film/edit.jsp").forward(request, response);
			return;
		}
		
		if (!NumberUtils.isCreatable(idRegistaParams)) {
			
			request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
			request.getRequestDispatcher("home").forward(request, response);
			return;
			
		}
		
		filmInstance.setId(Long.parseLong(idfilmParams));
		filmServiceIstance.aggiorna(filmInstance);
		response.sendRedirect("ExecuteListFilmServlet?operationResult=SUCCESS");
		
	}

}
