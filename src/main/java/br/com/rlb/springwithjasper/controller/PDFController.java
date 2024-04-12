package br.com.rlb.springwithjasper.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class PDFController {

    @GetMapping()
    public ResponseEntity<byte[]> generatePDF() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        // Configuração da fonte e tamanho
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);

        // Parágrafo com título centralizado e fonte tamanho 18
        Paragraph titulo = new Paragraph("Exemplo de Relatório com iText em Java", fontTitulo);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);

        PdfWriter.getInstance(document, baos);
        document.open();
        document.add(titulo);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Este é um relatório simples gerado usando iText."));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Você pode adicionar mais parágrafos, tabelas e outros elementos conforme necessário."));
        document.add(new Paragraph(" "));


        PdfPTable table = new PdfPTable(3);
        // Adicionando títulos das colunas
        table.addCell("Coluna 1");
        table.addCell("Coluna 2");
        table.addCell("Coluna 3");
        // Adicionando linhas e células com dados
        table.addCell("Dado 1");
        table.addCell("Dado 2");
        table.addCell("Dado 3");
        table.addCell("Dado 4");
        table.addCell("Dado 5");
        table.addCell("Dado 6");
        // Adicionando a tabela ao documento
        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // Criando e adicionando gráfico
        JFreeChart chart = createChart();
        String imagePath = "chart.png";
        ChartUtils.saveChartAsPNG(new java.io.File(imagePath), chart, 500, 300);
        com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imagePath);
        document.add(image);

        document.close();
        System.out.println("Relatório gerado com sucesso!");

        byte[] pdfBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = "relatorio.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

    }


    private static JFreeChart createChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "Categoria 1", "Tipo 1");
        dataset.addValue(2.0, "Categoria 1", "Tipo 2");
        dataset.addValue(3.0, "Categoria 1", "Tipo 3");
        dataset.addValue(4.0, "Categoria 2", "Tipo 1");
        dataset.addValue(5.0, "Categoria 2", "Tipo 2");
        dataset.addValue(6.0, "Categoria 2", "Tipo 3");

        JFreeChart chart = ChartFactory.createBarChart(
                "Gráfico de Barras", // título do gráfico
                "Categoria",         // rótulo do eixo X
                "Valor",             // rótulo do eixo Y
                dataset             // dados
        );
        return chart;
    }
}
