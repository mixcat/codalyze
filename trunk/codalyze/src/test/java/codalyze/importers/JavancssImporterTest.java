package codalyze.importers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import codalyze.entity.JavancssImports;

public class JavancssImporterTest {

	public static void main(String[] args) {
		System.out.println("porco");
	}
    private static JdbcTemplate jdbcTemplate;
    private static JavancssImporter importer;
    private static Date date;

    private String metadata = "metadata";
	private static SessionFactory sessionFactory;

    @BeforeClass
    public static void befeoreClass() throws IOException {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:test-context.xml");
        jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");   
        importer = (JavancssImporter) ctx.getBean("javancssImporter");
        date = new Date();
    }
    
    @Test
    public void testTest() {
    	JavancssImports javancssImports = new JavancssImports();
    	//sessionFactory.openSession().save(javancssImports);
    }

    @Test
    public void testInsertComplete() throws Exception {
        int count = count("javancss_packages") + count("javancss_objects") + count("javancss_functions");
        Document report = getReport("<javancss><packages>" + getPackage("pkg") + "</packages><objects>"+getObject("obj")+"</objects><functions>"+getFunction("func")+"</functions></javancss>");
        int reportId = importer.importReport(report, date, metadata);
        assertEquals(count+3, count("javancss_packages") + count("javancss_objects") + count("javancss_functions"));
    }
   
    @Test
    public void testInsertPackage() throws Exception {
        int count = count("javancss_packages");
        Document report = getReport("<javancss><packages>" + getPackage("pkg") + "</packages><objects>"+getObject("obj")+"</objects></javancss>");
        int reportId = importer.importReport(report, date, metadata);
        assertEquals(count+1, count("javancss_packages"));
    }
   
    @Test
    public void testInsertObject() throws Exception {
        int count = count("javancss_objects");
        Document report = getReport("<javancss><objects>"+getObject("obj")+"</objects></javancss>");
        int reportId = importer.importReport(report, date, metadata);
        assertEquals(count+1, count("javancss_objects"));
    }
   
    @Test
    public void testInsertFunction() throws Exception {
        int count = count("javancss_functions");
        Document report = getReport("<javancss><functions>"+getFunction("func")+"</functions></javancss>");
        int reportId = importer.importReport(report, date, metadata);
        assertEquals(count+1, count("javancss_functions"));
    }
   
    @Test
    public void testInsertZeroPackage() throws Exception {
        int count = count("javancss_packages");
        Document report = getReport("<javancss><packages></packages></javancss>");
        importer.importReport(report, date, metadata);
        assertEquals(count, count("javancss_packages"));
    }
   
    @Test
    public void testInsertIncompletePackageThrowsException() throws Exception {
        Document report = getReport("<javancss><packages><package><name></name></package></packages></javancss>");
        try {
            importer.importReport(report, date, metadata);
            fail();
        } catch (Exception e) {}
    }
   
    @Test
    public void testInsertIncompletePackageThrowsExceptionAndRollback() throws Exception {
        int count = count("javancss_packages");
        Document report = getReport("<javancss><packages>" + getPackage("pkg") + "<package><name></name></package></packages></javancss>");
        try {
            importer.importReport(report, date, metadata);
            fail();
        } catch (Exception e) {}
        assertEquals(count, count("javancss_packages"));
    }
   
    private int count(String table) {
        return jdbcTemplate.queryForInt("SELECT count(*) from " + table);
    }
   
    private Document getReport(String xmlReport) throws DocumentException {
        InputStream javaNcssXmlReport = new ByteArrayInputStream(xmlReport.getBytes());
        Document report = new SAXReader().read(javaNcssXmlReport);
        return report;
    }
    
    @Before
    public void setUp() {
    	sessionFactory.getCurrentSession().beginTransaction();
    }
   
    public String getPackage(String name) {
        return  "<package>"
        + "<name>"+name+"</name>"
        + "<classes>17</classes>"
        + "<functions>80</functions>"
        + "<ncss>192</ncss>"
        + "<javadocs>21</javadocs>"
        + "<javadoc_lines>81</javadoc_lines>"
        + "<single_comment_lines>1</single_comment_lines>"
        + "<multi_comment_lines>108</multi_comment_lines>" +
        "</package>";
    }
   
    public String getObject(String name) {
        return "<object>"
        + "<name>"+name+"</name>"
        + "<ncss>33</ncss>"
        + "<functions>3</functions>"
        + "<classes>0</classes>"
        + "<javadocs>1</javadocs>"
        + "</object>";
    }
   
    public String getFunction(String name) {
        return "<function>"
        + "<name>"+name+"</name>"
        + "<ncss>2</ncss>"
        + "<ccn>1</ccn>"
        + "<javadocs>5</javadocs>"
        + "</function>";
    }
   
   
   
}