package it.prova.raccoltafilm.web.servlet.utente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import it.prova.raccoltafilm.model.Regista;
import it.prova.raccoltafilm.model.Sesso;
import it.prova.raccoltafilm.model.StatoUtente;
import it.prova.raccoltafilm.model.Utente;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.UtenteService;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteSearchUtenteServlet
 */
@WebServlet("/admin/ExecuteSearchUtenteServlet")
public class ExecuteSearchUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UtenteService utenteServiceIstance;

	public ExecuteSearchUtenteServlet() {
		this.utenteServiceIstance = MyServiceFactory.getUtenteServiceInstance();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nomeParam = request.getParameter("nome");
		String cognomeParam = request.getParameter("cognome");
		String usernameParam = request.getParameter("userName");
		String dataDiCreazioneParam = request.getParameter("dateCreated");
		String statoParam = request.getParameter("stato");

		StatoUtente statoParsed = StringUtils.isNotBlank(statoParam) ? StatoUtente.valueOf(statoParam) : null;
		Utente example = new Utente(usernameParam, null, nomeParam, cognomeParam,
				UtilityForm.parseDateArrivoFromString(dataDiCreazioneParam));
		example.setStato(statoParsed);
		try {
			request.setAttribute("utenti_list_attribute", utenteServiceIstance.findByExample(example));
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/utente/search.jsp").forward(request, response);
			return;
		}
		request.getRequestDispatcher("/utente/list.jsp").forward(request, response);
	}

}
