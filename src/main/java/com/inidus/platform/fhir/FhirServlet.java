package com.inidus.platform.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import com.inidus.platform.fhir.allergy.AllergyProvider;
import com.inidus.platform.fhir.condition.ConditionProvider;
import com.inidus.platform.fhir.medication.MedicationStatementProvider;
import com.inidus.platform.fhir.procedure.ProcedureProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The RESTful server responding to FHIR requests
 */
@WebServlet(urlPatterns = {"/fhir/**"}, displayName = "inidus FHIR Adaptor")
@Component
public class FhirServlet extends RestfulServer {
    @Autowired
    AllergyProvider allergyProvider;
    @Autowired
    ConditionProvider conditionProvider;
    @Autowired
    MedicationStatementProvider medicationStatementProvider;
    @Autowired
    ProcedureProvider procedureProvider;

    public FhirServlet() {
        super(FhirContext.forDstu3());
    }

    @Override
    protected void initialize() throws ServletException {
        super.initialize();

        getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

        List<IResourceProvider> providers = new ArrayList<>();
        providers.add(this.allergyProvider);
        providers.add(this.conditionProvider);
        providers.add(this.medicationStatementProvider);
        providers.add(this.procedureProvider);
        setResourceProviders(providers);

        registerInterceptor(new ResponseHighlighterInterceptor());
        setDefaultPrettyPrint(true);
        setDefaultResponseEncoding(EncodingEnum.JSON);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeadersOriginToAny(response);
        super.doGet(request, response);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeadersOriginToAny(response);
        super.doOptions(request, response);
    }

    private void setCorsHeadersOriginToAny(HttpServletResponse response) {
        String m = "No protection against cross site attacks (Access-Control-Allow-Origin set to \"*\")";
        Logger.getLogger(getClass()).info(m);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
    }
}
