package org.jolly.ch6;

import java.util.*;
import java.util.concurrent.*;

public class TravelQuotes {
    private final ExecutorService executor;

    public TravelQuotes(ExecutorService executor) {
        this.executor = executor;
    }

    private static class QuoteTask implements Callable<TravelQuote> {
        private final TravelCompany company;
        private final TravelInfo travelInfo;

        private QuoteTask(TravelCompany company, TravelInfo travelInfo) {
            this.company = company;
            this.travelInfo = travelInfo;
        }

        @Override
        public TravelQuote call() throws Exception {
            return company.solicitQuote(travelInfo);
        }

        public TravelQuote getFailureQuote(Throwable cause) {
            return null;
        }

        public TravelQuote getTimeoutQuote(Throwable cause) {
            return null;
        }
    }

    public List<TravelQuote> getRankedTravelQuotes(TravelInfo travelInfo, Set<TravelCompany> companies,
                                                   Comparator<TravelQuote> ranking, long time, TimeUnit unit) throws InterruptedException {
        List<QuoteTask> tasks = new ArrayList<>();
        for (TravelCompany company : companies) {
            tasks.add(new QuoteTask(company, travelInfo));
        }

        List<Future<TravelQuote>> futures = executor.invokeAll(tasks, time, unit);

        List<TravelQuote> quotes = new ArrayList<>(tasks.size());
        Iterator<QuoteTask> taskIter = tasks.iterator();
        for (Future<TravelQuote> f : futures) {
            QuoteTask task = taskIter.next();
            try {
//                f.isCancelled()
//                f.isDone()
                quotes.add(f.get());
            } catch (ExecutionException e) {
                quotes.add(task.getFailureQuote(e.getCause()));
            } catch (CancellationException e) {
                quotes.add(task.getTimeoutQuote(e));
            }
        }

        quotes.sort(ranking);
        return quotes;
    }


    public interface TravelQuote {}
    public interface TravelInfo {}
    public static class TravelCompany {
        TravelQuote solicitQuote(TravelInfo travelInfo) {
            return null;
        }
    }
}
