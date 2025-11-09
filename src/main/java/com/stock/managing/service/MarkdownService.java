package com.stock.managing.service;

import org.springframework.stereotype.Service;

import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.Arrays;

@Service
public class MarkdownService {

    private final Parser parser;
    private final HtmlRenderer renderer;
    private final Safelist safelist;

    public MarkdownService() {
        MutableDataSet options = new MutableDataSet();
        // GitHub-Flavored Markdown extensions
        options.set(Parser.EXTENSIONS, Arrays.asList(
                TablesExtension.create(),
                AutolinkExtension.create(),
                StrikethroughExtension.create(),
                TaskListExtension.create()
        ));

        this.parser = Parser.builder(options).build();
        // Allow raw HTML in markdown; we'll sanitize after rendering
        this.renderer = HtmlRenderer.builder(options)
                .escapeHtml(false)
                .build();

        // Relaxed safelist plus tables and images allowed
        this.safelist = Safelist.relaxed()
                .addTags("table", "thead", "tbody", "tfoot", "tr", "th", "td")
                .addAttributes("img", "src", "alt", "title")
                .addAttributes("a", "href", "title", "target", "rel");
    }

    public String toHtml(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }
        Node document = parser.parse(markdown);
        String html = renderer.render(document);
        // Sanitize output to prevent XSS while keeping tables/images/links
        return Jsoup.clean(html, safelist);
    }
}
