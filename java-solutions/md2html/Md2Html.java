package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Md2Html {
	public static void main(String[] fileNames) {
		// not enough args?
		String mdFileName = fileNames[0];
		String htmlFileName = fileNames[1];

		StringBuilder sb = new StringBuilder();

		try (TextExtractor extractor = new TextExtractor(mdFileName)) {
			TextExtractor.ParagraphManager pManager = extractor.splitByParagraphs();
			TextManager tManager = new TextManager();
			HtmlManager htmlManager = new HtmlManager();

			while (pManager.hasNextParagraph()) {
				List<String> paragraph = pManager.nextParagraph();
				String firstLine = paragraph.get(0);

				if (tManager.isHeader(firstLine)) {
					int typeOfHeader = tManager.headerType(firstLine);

					sb.append(htmlManager.createHeaderTag(typeOfHeader, true));
					tManager.processParagraphText(
							typeOfHeader + 1, paragraph, sb, htmlManager
					);
					sb.append(htmlManager.createHeaderTag(typeOfHeader, false));
					sb.append(System.lineSeparator());
				} else {
					sb.append("<p>");
					tManager.processParagraphText(
							0, paragraph, sb, htmlManager
					);
					sb.append("</p>");
					sb.append(System.lineSeparator());
				}
			}

		} catch (FileNotFoundException e) {
			System.err.println("File was not found: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error while creating TextExtractor instance" + e.getMessage());
		}

		try (BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(htmlFileName),
				StandardCharsets.UTF_8
		))) {
			output.append(sb);
		} catch (IOException e) {
			System.err.println("Something went wrong while writing to file " + e.getMessage());
		}
	}
}
