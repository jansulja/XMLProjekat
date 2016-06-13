package xquery;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.eval.EvalResult;
import com.marklogic.client.eval.EvalResultIterator;
import com.marklogic.client.eval.ServerEvaluationCall;

import database.Config;

/**
 *
 * [PRIMER 9]
 *
 * Primer demonstrira izvršavanje XQuery upita iz MarkLogic Java API-ja.
 *
 */
public class XQueryInvoker {

	private static DatabaseClient client;

	private static final String prefix = "resource/xquery/";

	private static Logger log = Logger.getLogger(XQueryInvoker.class);


	public static void run() throws IOException {

		log.info("[INFO] " + XQueryInvoker.class.getSimpleName());

		// Initialize the database client
		client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password, Config.authType);

		// Script file which is to be invoked
		String filePath = prefix + "retrieve-flwor.xqy";

		log.info("[INFO] Invoking: " + filePath);

		// Initialize XQuery invoker object
		ServerEvaluationCall invoker = client.newServerEval();

		// Read the file contents into a string object
		String query = readFile(filePath, StandardCharsets.UTF_8);

		// Invoke the query
		invoker.xquery(query);

		// Interpret the results
		EvalResultIterator response = invoker.eval();

		log.info("[INFO] Response: ");

		if (response.hasNext()) {

			for (EvalResult result : response) {
				log.info("\n" + result.getString());
			}
		} else {
			log.info("your query returned an empty sequence.");
		}

		// Release the client
		client.release();

		log.info("[INFO] End.");
	}

	/**
	 * Convenience method for reading file contents into a string.
	 */
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void main(String[] args) throws IOException {
		run();
	}

}
