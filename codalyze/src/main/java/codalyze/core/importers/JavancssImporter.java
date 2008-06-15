package codalyze.core.importers;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.transaction.annotation.Transactional;

import codalyze.entity.JavancssFunctions;
import codalyze.entity.JavancssImports;
import codalyze.entity.JavancssObjects;
import codalyze.entity.JavancssPackages;

public class JavancssImporter {

	Logger log = Logger.getLogger(JavancssImporter.class);
	private SessionFactory sessionFactory;

	public JavancssImporter(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public JavancssImporter() {
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public int importReport(final Document xmlReport, final Date date, final String metadata) {
		Session session = sessionFactory.getCurrentSession();
		JavancssImports imports = new JavancssImports(date,metadata);
		session.save(imports);
		int packages=0;
		for (Node node : (List<Node>) xmlReport.selectNodes("//packages/package")) {
			session.save(new JavancssPackages(
				imports,
				stringValue(node, "name"),
				value(node, "classes"),
				value(node, "functions"),
				value(node, "ncss"),
				value(node, "javadocs"),
				value(node, "javadoc_lines"),
				value(node, "single_comment_lines"),
				value(node, "multi_comment_lines")
			));
			packages++;
		}
		log.info("Packages section. Imported " + packages + " records.");
		int objects = 0;
		for (Node node : (List<Node>) xmlReport.selectNodes("//objects/object")) {
			session.save(new JavancssObjects(
				imports,
				stringValue(node, "name"),
				value(node, "ncss"),
				value(node, "functions"),
				value(node, "classes"),
				value(node, "javadocs")
			));
			objects++;
		}
		log.info("Objects section. Imported " + objects + " records.");
		
		int functions = 0;
		for (Node node : (List<Node>) xmlReport.selectNodes("//functions/function")) {
			session.save(new JavancssFunctions(
				imports,
				stringValue(node, "name"),
				value(node, "ncss"),
				value(node, "ccn"),
				value(node, "javadocs")
			));
			functions++;
		}
		log.info("Functions section. Imported " + functions + " records.");

		return 0;
	}

	private String stringValue(Node node, String xpath) {
		Node selectedNode = node.selectSingleNode(xpath);
		return (selectedNode != null) ? selectedNode.getStringValue() : null;
	}

	private int value(Node node, String xpath) {
		Node selectedNode = node.selectSingleNode(xpath);
		//if (selectedNode==null) throw new RuntimeException("No value");
		return (selectedNode != null) ? Integer.parseInt(selectedNode.getStringValue()) : null;
	}

}