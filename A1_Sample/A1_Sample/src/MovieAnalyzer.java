import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
class Movie {
  private String Series_Title;
  private int Released_Year;
  private String Certificate;
  private String Runtime;
  private String Genre;
  private float IMDB_Rating;
  private String Overview;
  private int Meta_score;
  private String Director;
  private String Star1;
  private String Star2;
  private String Star3;
  private String Star4;
  private long No_of_Votes;
  private String Gross;
  public Movie(String Series_Title,
                 int Released_Year,
                 String Certificate,
                 String Runtime,
                 String Genre,
                 float IMDB_Rating,
                 String Overview,
                 int Meta_score,
                 String Director,
                 String Star1,
                 String Star2,
                 String Star3,
                 String Star4,
                 long No_of_Votes,
                 String Gross) {
    this.Series_Title = Series_Title;
    this.Released_Year = Released_Year;
    this.Certificate = Certificate;
    this.Runtime = Runtime;
    this.Genre = Genre;
    this.IMDB_Rating = IMDB_Rating;
    this.Overview = Overview;
    this.Meta_score = Meta_score;
    this.Director = Director;
    this.Star1 = Star1;
    this.Star2 = Star2;
    this.Star3 = Star3;
    this.Star4 = Star4;
    this.No_of_Votes = No_of_Votes;
    this.Gross = Gross;
  }

  @Override
  public String toString() {
    return "Movie{Series_Title='"
                + Series_Title
                + "'"
                + ", Released_Year='"
                + Released_Year
                + "'"
                + ", Certificate='"
                + Certificate
                + "'"
                + ", Runtime='"
                + Runtime
                + "'"
                + ", Genre='"
                + Genre
                + "'"
                + ", IMDB_Rating='"
                + IMDB_Rating
                + "'"
                + ", Overview='"
                + Overview
                + "'"
                + ", Meta_score='"
                + Meta_score
                + "'"
                + ", Director='"
                + Director
                + "'"
                + ", Star1='"
                + Star1
                + "'"
                + ", Star2='"
                + Star2
                + "'"
                + ", Star3='"
                + Star3
                + "'"
                + ", Star4='"
                + Star4
                + "'"
                + ", No_of_Votes='"
                + No_of_Votes
                + "'"
                + ", Gross='"
                + Gross
                + "'";
  }

  public String getSeries_Title() {
    return Series_Title;
  }

  public int getReleased_Year() {
    return Released_Year;
  }

  public String getCertificate() {
    return Certificate;
  }

  public String getRuntime() {
    return Runtime;
  }

  public String getGenre() {
    return Genre;
  }

  public double getIMDB_Rating() {
    return IMDB_Rating;
  }

  public String getOverview() {
    return Overview.replace("\"", " \"").replace(" \" \"", "\"").replace("\"", "\"\"");
  }

  public int getMeta_score() {
    return Meta_score;
  }

  public String getDirector() {
    return Director;
  }

  public String getStar1() {
    return Star1;
  }

  public String getStar2() {
    return Star2;
  }

  public String getStar3() {
    return Star3;
  }

  public String getStar4() {
    return Star4;
  }

  public long getNo_of_Votes() {
    return No_of_Votes;
  }

  public String getGross() {
    return Gross;
  }

  public Long getGrossLong() {
    String GrossLong = Gross.replace(",", "");
    return Long.parseLong(GrossLong);
  }
  public List<String> getGenreList() { return Arrays.stream(getGenre().split(", ")).toList(); }

  public List<List<String>> getStarsPairs() {
        List<String> Stars = new ArrayList<>();
        Stars.add(getStar1());
        Stars.add(getStar2());
        Stars.add(getStar3());
        Stars.add(getStar4());
        Collections.sort(Stars, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        List<List<String>> res = new ArrayList<>();
        for (int i = 0; i < Stars.size(); i++){
            for (int j = i + 1; j < Stars.size(); j++){
                List<String> temp = Stream.of(Stars.get(i), Stars.get(j)).toList();
                res.add(temp);
            }
        }
        return res;
    }

    public Set<String> getStars() {
        Set<String> Stars = new HashSet<>();
        Stars.add(getStar1());
        Stars.add(getStar2());
        Stars.add(getStar3());
        Stars.add(getStar4());
        return Stars;
    }
}
public class MovieAnalyzer {
    Supplier<Stream<Movie>> MovieString;
    public MovieAnalyzer(String dataset_path) throws IOException {
        this.MovieString = () -> {
            try {
                return Files.lines(Paths.get(dataset_path), StandardCharsets.UTF_8)
                        .map(MovieAnalyzer::splitCSV).skip(1)
                        .map(a -> new Movie(a[1], Integer.parseInt(a[2]), a[3], a[4], a[5], Float.parseFloat(a[6]), a[7],
                                a[8].length() > 0 ? Integer.parseInt(a[8]) : 0, a[9], a[10], a[11], a[12], a[13],
                                Long.parseLong(a[14]), a[15]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    //todo:
    public Map<Integer, Integer> getMovieCountByYear() throws IOException, URISyntaxException {
        Map<Integer, Long> Map = MovieString.get().collect(Collectors.groupingBy(Movie::getReleased_Year,
                TreeMap::new, Collectors.counting())).descendingMap();
        TreeMap<Integer, Integer> target = new TreeMap<>();
        for (Integer l : Map.keySet()) {
            target.put(l, Integer.parseInt(Map.get(l).toString()));
        }
        return target.descendingMap();
    }

    public Map<String, Integer> getMovieCountByGenre() {
        Set<String> genres = new HashSet<>();
        List<String> list = MovieString.get().map(Movie::getGenre).toList();
        TreeMap<String, Integer> target = new TreeMap<>();
        for (String entry : list) {
            String[] genre_for_one = entry.split(", ");
            for (String genre: genre_for_one) {
                genres.add(genre);
            }
        }
        for (String entry: genres) {
//            System.out.println(entry);
            Long a =
                    MovieString.get().filter(movie -> movie.getGenreList().contains(entry)).collect(Collectors.counting());
            target.put(entry, Integer.parseInt(a.toString()));
        }
        Map<String, Integer> result = new LinkedHashMap<>();
        target.entrySet().stream().sorted(java.util.Map.Entry.<String, Integer>comparingByValue().reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    public Map<List<String>, Integer> getCoStarCount() {
        List<List<String>> starsList = new ArrayList<>();
        MovieString.get().map(Movie::getStarsPairs).forEach(e -> {
            for (List<String> t: e) {
                starsList.add(t);
            }
        });
        LinkedHashMap<List<String>, Integer> target = new LinkedHashMap<>();
        for (List<String> t: starsList) {
            boolean flag = true;
            for (List<String> e: target.keySet()) {
                if (t.equals(e)) {
                    target.replace(t, target.get(t) + 1);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                target.put(t, 1);
            }
        }
        LinkedHashMap<List<String>, Integer> result = new LinkedHashMap<>();
        target.entrySet().stream().sorted(java.util.Map.Entry.<List<String>, Integer>comparingByValue().reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    public List<String> getTopMovies(int top_k, String by) {
        List<String> result = new ArrayList<>();
        if (by.equals("runtime")) {
            result =
                    MovieString.get().sorted(Comparator.comparing(Movie::getSeries_Title
                    )).sorted(Comparator.comparing(movie -> Integer.parseInt(movie.getRuntime().split(
                            " ")[0]), Comparator.reverseOrder())).map(Movie::getSeries_Title).limit(top_k).toList();
        }
        else {
            result =
                    MovieString.get().sorted(Comparator.comparing(Movie::getSeries_Title
                    )).sorted(Comparator.comparing(movie -> movie.getOverview().length(),
                            Comparator.reverseOrder())).map(Movie::getSeries_Title).limit(top_k).toList();
        }
        return result;
    }

    public List<String> getTopStars(int top_k, String by) {
        Map<String, List<Double>> starsList = new LinkedHashMap<>();
        if (by.equals("rating")) {
            MovieString.get().forEach(movie -> {
                for (String star: movie.getStars()) {
                    if (starsList.containsKey(star)) {
                        starsList.get(star).add(movie.getIMDB_Rating());
                    }
                    else {
                        List<Double> t = new ArrayList<>();
                        t.add(movie.getIMDB_Rating());
                        starsList.put(star, t);
                    }
                }
            });
        }
        else {
            MovieString.get().filter(movie -> !movie.getGross().equals("")).forEach(movie -> {
                for (String star: movie.getStars()) {
                    if (starsList.containsKey(star)) {
                        starsList.get(star).add((double) movie.getGrossLong());
                    }
                    else {
                        List<Double> t = new ArrayList<>();
                        t.add((double) movie.getGrossLong());
                        starsList.put(star, t);
                    }
                }
            });
        }
        ArrayList<String> result = new ArrayList<>();
        LinkedHashMap<String, Double> target = new LinkedHashMap<>();
        LinkedHashMap<String, Double> targetAfter = new LinkedHashMap<>();
        for (Map.Entry<String, List<Double>> e : starsList.entrySet()) {
            target.put(e.getKey(), e.getValue().stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        }
//        Set<String> starsList = new HashSet<>();
//
//        MovieString.get().map(Movie::getStars).forEach(e -> {
//            for (String t: e) {
//                starsList.add(t);
//            }
//        });
//
//        ArrayList<String> result = new ArrayList<>();
//        LinkedHashMap<String, Double> target = new LinkedHashMap<>();
//        LinkedHashMap<String, Double> targetAfter = new LinkedHashMap<>();
//        for (String star: starsList) {
//            if (by.equals("rating")) {
//                double rating =
//                        MovieString.get().filter(movie -> movie.getStars().contains(star)).collect(Collectors.averagingDouble(Movie::getIMDB_Rating));
//                target.put(star, rating);
//            }
//            else {
//                double gross =
//                        MovieString.get().filter(movie -> !movie.getGross().equals("")).filter(movie -> movie.getStars().contains(star)).collect(Collectors.averagingLong(Movie::getGrossLong));
//                target.put(star, gross);
//            }
//        }
        target.entrySet().stream().sorted(Map.Entry.comparingByKey()).sorted(java.util.Map.Entry.<String, Double>comparingByValue().reversed()).forEachOrdered(e -> targetAfter.put(e.getKey(), e.getValue()));
        int count = 0;
        for (Map.Entry<String, Double> entry: targetAfter.entrySet()){
            if (count == top_k){
                break;
            }
            result.add(entry.getKey());
            count++;
        }
        return result;
    }

    public Map<String, Double> getTopStarsTest(int top_k, String by) {
        Set<String> starsList = new HashSet<>();
        MovieString.get().map(Movie::getStars).forEach(e -> {
            for (String t: e) {
                starsList.add(t);
            }
        });
        ArrayList<String> result = new ArrayList<>();
        LinkedHashMap<String, Double> target = new LinkedHashMap<>();
        LinkedHashMap<String, Double> targetAfter = new LinkedHashMap<>();
        for (String star: starsList) {
            if (by.equals("rating")) {
                double rating =
                        MovieString.get().filter(movie -> movie.getStars().contains(star)).collect(Collectors.averagingDouble(Movie::getIMDB_Rating));
                target.put(star, rating);
            }
            else {
                double gross =
                        MovieString.get().filter(movie -> movie.getStars().contains(star)).collect(Collectors.averagingLong(Movie::getGrossLong));
                target.put(star, gross);
            }
        }
        target.entrySet().stream().sorted(java.util.Map.Entry.<String, Double>comparingByValue().reversed()).forEachOrdered(e -> targetAfter.put(e.getKey(), e.getValue()));
        return targetAfter;
    }
    public List<String> searchMovies(String genre, float min_rating, int max_runtime) {
        List<String> result =
                MovieString.get().filter(movie -> movie.getGenreList().contains(genre)).filter(movie -> movie.getIMDB_Rating() >= min_rating).filter(movie -> Integer.parseInt(movie.getRuntime().split(
                        " ")[0]) <= max_runtime).sorted(Comparator.comparing(Movie::getSeries_Title
        )).map(Movie::getSeries_Title).toList();
        return result;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        MovieAnalyzer m = new MovieAnalyzer("resources/imdb_top_500.csv");
        List<Long> result = m.MovieString.get().map(Movie::getGrossLong).toList();
        for (long r: result) {
            System.out.println(r);
        }

//        Map<String, Double> result = m.getTopStarsTest(20, "rating");
//        for (Map.Entry<String, Double> entry: result.entrySet()) {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }
    }

    public static String[] splitCSV(String txt) {

        String reg = "\\G(?:^|,)(?:\"([^\"]*+(?:\"\"[^\"]*+)*+)\"|([^\",]*+))";

        Matcher matcherMain = Pattern.compile(reg).matcher("");
        Matcher matcherQuote = Pattern.compile(" \"\"").matcher("");

        matcherMain.reset(txt);
        List<String> strList = new ArrayList<>();
        while (matcherMain.find()) {
            String field;
            if (matcherMain.start(2) >= 0) {
                field = matcherMain.group(2);
            } else {
                field = matcherQuote.reset(matcherMain.group(1)).replaceAll("\"");
            }
            strList.add(field);
        }
        String[] strArray = new String[strList.size()];
        strArray = strList.toArray(strArray);
        return strArray;
    }
}