package net.kirkegaard.alfrescocmisatom;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Very simple test of Alfresco CMIS over ATOM
 */
public class App {
    public static void main(String[] args) {
        // Setting the scene to use the public facing alfresco CMISATOM test instance
        // http://www.alfresco.com/cmis
        final String USER = "admin";
        final String PASSWORD = "admin";
        final String ATOMPUB_URL = "http://cmis.alfresco.com/cmisatom";

        // Query to execute in this very simple test
        final String QUERY = "SELECT * FROM cmis:document WHERE cmis:name LIKE 'test%'";

        Map<String, String> parameter = new HashMap<String, String>();

        // Set the user credentials
        parameter.put(SessionParameter.USER, USER);
        parameter.put(SessionParameter.PASSWORD, PASSWORD);

//        // Setting another custom AuthenticationProvider
//        // See: Custom Authentication Provider
//        // http://chemistry.apache.org/java/developing/client/dev-client-bindings.html
//        // https://chemistry.apache.org/java/0.10.0/maven/apidocs/org/apache/chemistry/opencmis/client/bindings/spi/AbstractAuthenticationProvider.html
//        parameter.put(SessionParameter.AUTHENTICATION_PROVIDER_CLASS, "org.example.opencmis.MyAuthenticationProvider");
//        parameter.put("org.example.opencmis.user", "cmisuser"); // MyAuthenticationProvider can get and evaluate this
//        parameter.put("org.example.opencmis.secret", "b3BlbmNtaXMgdXNlcg==");

        // Specify the connection settings
        parameter.put(SessionParameter.ATOMPUB_URL, ATOMPUB_URL);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

        // Set the alfresco object factory
        parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

        // Create a session
        SessionFactory factory = SessionFactoryImpl.newInstance();
        Session session = factory.getRepositories(parameter).get(0).createSession();

        // Checking what repositories we find at this endpoint
        List<Repository> repositories = factory.getRepositories(parameter);
        for (Repository r : repositories) {
            System.out.println("Found repository: " + r.getName());
        }

        // Trying a query
        // Session.query(String statement, boolean searchAllVersions)
        ItemIterable<QueryResult> q = session.query(QUERY, false);

        // Did it work?
        System.out.println("***results from query " + QUERY);

        int i = 1;
        for (QueryResult qr : q) {
            System.out.println("--------------------------------------------\n" + i + " , "
                    + qr.getPropertyByQueryName("cmis:objectTypeId").getFirstValue() + " , "
                    + qr.getPropertyByQueryName("cmis:name").getFirstValue() + " , "
                    + qr.getPropertyByQueryName("cmis:createdBy").getFirstValue() + " , "
                    + qr.getPropertyByQueryName("cmis:objectId").getFirstValue() + " , "
                    + qr.getPropertyByQueryName("cmis:contentStreamFileName").getFirstValue() + " , "
                    + qr.getPropertyByQueryName("cmis:contentStreamMimeType").getFirstValue() + " , "
                    + qr.getPropertyByQueryName("cmis:contentStreamLength").getFirstValue());
            i++;
        }
    }
}
