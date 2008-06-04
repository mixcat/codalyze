package codalyze.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import codalyze.importers.JavancssImporter;

public class ImporterJavancssCLI {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws ParseException, java.text.ParseException, DocumentException, FileNotFoundException {
		
		Options options = new Options();
		
		Option optionDate = OptionBuilder.withArgName("date")
			.hasArg()
			.withDescription("Date for which this javancss report is relevant. [yyyy-MM-dd]")
			.isRequired()
			.create("date");
		
		Option optionInput = OptionBuilder.withArgName("input")
			.hasArg()
			.withDescription("JavaNcss Input file")
			.isRequired()
			.create("input");
		
		Option optionMetadata = OptionBuilder.withArgName("metadata")
			.hasArg()
			.withDescription("Metadata i.e. branches, tags, special notes etc. ie: \"project:seacat, iteration:trunk, event:removed old code\"")
			.isRequired()
			.create("meta");
		
		options.addOption(optionDate);
		options.addOption(optionInput);
		options.addOption(optionMetadata);
		
		CommandLine commandLine = getCommandLine(options, args);
		
		String dateValue = commandLine.getOptionValue(optionDate.getArgName());
		String inputValue = commandLine.getOptionValue(optionInput.getArgName());
		String metadataValue = commandLine.getOptionValue(optionMetadata.getOpt());
		
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateValue);
		FileInputStream inputStream = new FileInputStream(new File(inputValue));
		
		ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:application-context.xml");
		JavancssImporter javancssImporter = (JavancssImporter) ctx.getBean("javancssImporter");
		javancssImporter.importReport(getReport(inputStream), date, metadataValue );
		
		System.out.println(dateValue + " " + inputValue + " " + metadataValue);
		
		System.out.println(options.getRequiredOptions());
	}
	
	private static CommandLine getCommandLine(Options options, String[] args) throws ParseException {
		return new GnuParser().parse(options, args);
	}
	
	private static Document getReport(InputStream inputStream) throws DocumentException {
		Document report = new SAXReader().read(inputStream);
		return report;
	}
}