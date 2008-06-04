package codalyze.importers;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class JavancssImporter {

    private static final PreparedStatementCreatorFactory INSERT_IMPORTS = getStatementCreatorFactory (
            "insert into javancss_imports(date, metadata) VALUES(?,?)",
            new int[] {Types.DATE, Types.VARCHAR });
   
    private static final PreparedStatementCreatorFactory INSERT_PACKAGE = getStatementCreatorFactory (
            "insert into javancss_packages(import_id, name, ncss, classes, functions, javadocs, javadoc_lines, single_comment_lines, multi_comment_lines)" +
            "VALUES(?,?,?,?,?,?,?,?,?)",
            new int[] { Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER});
   
    private static final PreparedStatementCreatorFactory INSERT_OJBJECT = getStatementCreatorFactory (
            "insert into javancss_objects(import_id, name, ncss, classes, functions, javadocs) VALUES(?,?,?,?,?,?)",
            new int[] { Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER } );
   
    private static final PreparedStatementCreatorFactory INSERT_FUNCTION = getStatementCreatorFactory (
            "insert into javancss_functions(import_id, name, ncss, ccn, javadocs) VALUES(?,?,?,?,?)",
            new int[] {Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER});
   
    private static PreparedStatementCreatorFactory getStatementCreatorFactory (String query, int[] types) {
        return new PreparedStatementCreatorFactory(query, types);
    }
   
    private final TransactionTemplate transactionTemplate;
   
    private final JdbcTemplate jdbcTemplate;

    public JavancssImporter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(jdbcTemplate.getDataSource());
        transactionTemplate = new TransactionTemplate(transactionManager);
    }
   
    @SuppressWarnings("unchecked")
    public int importReport(final Document xmlReport, final Date date, final String metadata) {
        transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                update(INSERT_IMPORTS, new Object[] { date, metadata });
                int id = jdbcTemplate.queryForInt("CALL IDENTITY()");
                for (Node node : (List<Node>) xmlReport.selectNodes("//packages/package")) {
                    update(INSERT_PACKAGE, new Object[] {
                        id,
                        value(node, "name"),
                        value(node, "classes"),
                        value(node, "functions"),
                        value(node, "ncss"),
                        value(node, "javadocs"),
                        value(node, "javadoc_lines"),
                        value(node, "single_comment_lines"),
                        value(node, "multi_comment_lines")
                    });
                };
                for (Node node : (List<Node>) xmlReport.selectNodes("//objects/object")) {
                    update(INSERT_OJBJECT, new Object[] {
                        id,
                        value(node, "name"),
                        value(node, "ncss"),
                        value(node, "functions"),
                        value(node, "classes"),
                        value(node, "javadocs"),
                    });
                }
                for (Node node : (List<Node>) xmlReport.selectNodes("//functions/function")) {
                    update(INSERT_FUNCTION, new Object[] {
                        id,
                        value(node, "name"),
                        value(node, "ncss"),
                        value(node, "ccn"),
                        value(node, "javadocs")
                    });
                }
                return null;
            }
        });
        return 0;
    }
   
    private String value(Node node, String xpath) {
        Node selectedNode = node.selectSingleNode(xpath);
        return (selectedNode != null) ? selectedNode.getStringValue() : null;
    }
   
    private void update(PreparedStatementCreatorFactory creatorFactory, Object[] values) {
        jdbcTemplate.update(creatorFactory.newPreparedStatementCreator(values));
    }

}