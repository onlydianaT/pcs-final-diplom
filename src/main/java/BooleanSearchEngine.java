import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> result = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        for (var listOfFiles : pdfsDir.listFiles()) {
            var doc = new PdfDocument(new PdfReader(listOfFiles));
            //Перебираем страницы PDF и получаем текст каждой страницы
            for (int j = 1; j <= doc.getNumberOfPages(); j++) {
                String word = "";
                Map<String, Integer> freqs = new HashMap<>();
                var page = doc.getPage(j);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");

                for (int i = 0; i < words.length; i++) {
                    word = words[i];
                    if (word.isEmpty()) {
                        continue;
                    } else {
                        word = word.toLowerCase();
                        freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                    }
                }
                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    String key = entry.getKey();
                    String nameOfFile = String.valueOf(listOfFiles.getName());
                    if (result.containsKey(key)) {
                        List<PageEntry> search = new ArrayList<>();
                        search = result.get(key);
                        search.add(new PageEntry(nameOfFile, j, freqs.get(key)));
                        Collections.sort(search, Comparator.comparing(PageEntry::getCount).reversed());
                        result.put(key, search);
                    } else {
                        List<PageEntry> search = new ArrayList<>();
                        search.add(new PageEntry(nameOfFile, j, freqs.get(key)));
                        Collections.sort(search, Comparator.comparing(PageEntry::getCount).reversed());
                        result.put(key, search);
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return result.get(word);
    }
}
