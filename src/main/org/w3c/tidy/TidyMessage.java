package org.w3c.tidy;

/**
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public final class TidyMessage
{

    /**
     * dont't instantiate.
     */
    private TidyMessage()
    {
    }

    /**
     * error gravity enumeration.
     * @author fgiust
     * @version $Revision$ ($Author$)
     */
    final static class Level
    {

        /**
         * level = summary.
         */
        public static final short SUMMARY = 0;

        /**
         * level = info.
         */
        public static final short INFO = 1;

        /**
         * level = warning.
         */
        public static final short WARNING = 2;

        /**
         * level = error.
         */
        public static final short ERROR = 3;

        /**
         * don't instantiate.
         */
        private Level()
        {
        }

    }

}