package barsnik96.chat.util;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public final class CollectionUtils {

    /**
     * Returns an (mutable) empty {@link HashSet} if given set is {@code null},
     * the set itself otherwise.
     *
     * @param set the set to null check
     * @param <T> the set type
     * @return an empty set if given set is {@code null}
     */
    public static <T> Set<T> emptySetIfNull(final Set<T> set) {
        return set == null ? new HashSet<>() : set;
    }
}
