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
    final class Level
    {

        /**
         * level = info.
         */
        public static final short INFO = 0;

        /**
         * level = warning.
         */
        public static final short WARNING = 1;

        /**
         * level = error.
         */
        public static final short ERROR = 2;

        /**
         * don't instantiate.
         */
        private Level()
        {
        }

    }

}