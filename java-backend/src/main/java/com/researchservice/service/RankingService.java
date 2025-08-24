package com.researchservice.service;

import com.researchservice.model.Paper;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private static final Set<String> STOPWORDS = Set.of(
            "a","an","the","and","or","of","for","to","in","on","with","by","about","from","into","at","as"
    );

    public List<Paper> rankAndDedup(String query, List<Paper> input) {
        if (input == null || input.isEmpty()) return Collections.emptyList();

        String[] qTokens = tokens(query);

        for (Paper p : input) {
            double sim = similarity(qTokens, tokens(p.getTitle() + " " + nullSafe(p.getAbstractText())));
            double recency = recencyBoost(p.getPublished()); // 0..0.3
            p.setScore(sim + recency);
        }

        List<Paper> sorted = input.stream()
                .sorted(Comparator.comparingDouble(Paper::getScore).reversed())
                .collect(Collectors.toList());

        List<Paper> deduped = new ArrayList<>();
        for (Paper c : sorted) {
            boolean dup = false;
            for (Paper k : deduped) {
                if (jaccard(tokens(c.getTitle()), tokens(k.getTitle())) >= 0.9) { dup = true; break; }
            }
            if (!dup) deduped.add(c);
        }
        return deduped;
    }

    private static String nullSafe(String s) { return s == null ? "" : s; }

    private static String[] tokens(String text) {
        if (text == null) return new String[0];
        return Arrays.stream(text.toLowerCase().replaceAll("[^a-z0-9 ]", " ").split("\\s+"))
                .filter(t -> t.length() > 1 && !STOPWORDS.contains(t))
                .toArray(String[]::new);
    }

    private static Map<String,Integer> count(String[] toks) {
        Map<String,Integer> m = new HashMap<>();
        for (String t : toks) m.put(t, m.getOrDefault(t, 0) + 1);
        return m;
    }

    private static double similarity(String[] a, String[] b) {
        Map<String,Integer> ca = count(a), cb = count(b);
        Set<String> vocab = new HashSet<>(ca.keySet()); vocab.addAll(cb.keySet());
        double dot=0, na=0, nb=0;
        for (String t : vocab) {
            int va = ca.getOrDefault(t,0), vb = cb.getOrDefault(t,0);
            dot += va * vb; na += va*va; nb += vb*vb;
        }
        if (na==0 || nb==0) return 0;
        return dot / (Math.sqrt(na)*Math.sqrt(nb));
    }

    private static double jaccard(String[] a, String[] b) {
        Set<String> sa = new HashSet<>(Arrays.asList(a));
        Set<String> sb = new HashSet<>(Arrays.asList(b));
        if (sa.isEmpty() && sb.isEmpty()) return 1.0;
        Set<String> inter = new HashSet<>(sa); inter.retainAll(sb);
        Set<String> uni = new HashSet<>(sa); uni.addAll(sb);
        return (double) inter.size() / (double) uni.size();
    }

    private static double recencyBoost(String published) {
        int year = safeYear(published);
        if (year == 0) return 0;
        int current = OffsetDateTime.now().getYear();
        int delta = Math.max(0, Math.min(5, current - year)); // cap
        return Math.max(0, 0.3 - 0.06 * delta);
    }

    private static int safeYear(String published) {
        if (published == null || published.isBlank()) return 0;
        try { return OffsetDateTime.parse(published).getYear(); } catch (DateTimeParseException ignore) {}
        try {
            String y = published.replaceAll("[^0-9]", "");
            if (y.length() >= 4) return Integer.parseInt(y.substring(0,4));
        } catch (Exception ignore) {}
        return 0;
    }
}
