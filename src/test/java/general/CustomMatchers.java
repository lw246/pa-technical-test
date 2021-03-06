package general;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsCollectionContaining;

public final class CustomMatchers {

    public static <T> Matcher<Iterable<? super T>> exactlyNItems(final int numberOfExpectMatches,
                                                                 final Matcher<? super T> elementMatcher) {
        return new IsCollectionContaining<T>(elementMatcher) {
            @Override
            protected boolean matchesSafely(Iterable<? super T> collection, Description mismatchDescription) {
                int count = 0;
                boolean isPastFirst = false;

                for (Object item : collection) {

                    if (elementMatcher.matches(item)) {
                        count++;
                    }
                    if (isPastFirst) {
                        mismatchDescription.appendText(", ");
                    }
                    elementMatcher.describeMismatch(item, mismatchDescription);
                    isPastFirst = true;
                }

                if (count != numberOfExpectMatches) {
                    mismatchDescription.appendText(". Expected exactly " + numberOfExpectMatches + " but got " + count);
                }
                return count == numberOfExpectMatches;
            }
        };
    }
}