package org.w3c.tidy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author fgiust
 * @version $Revision $ ($Author $)
 */
public class StreamInImplTest extends TestCase
{

    /**
     * logger.
     */
    private static Log log = LogFactory.getLog(StreamInImplTest.class);

    /**
     * test instance.
     */
    private StreamInImpl in;

    /**
     * Lexer instance saved in StreamInImpl.
     */
    private Lexer lexer;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        InputStream stream = new ByteArrayInputStream(new byte[]{0});
        in = new StreamInImpl(stream, Configuration.ASCII, 8);
        Report report = new Report();
        Configuration configuration = new Configuration(report);
        lexer = new Lexer(in, configuration, report);
        lexer.configuration = configuration;
        in.setLexer(lexer);
    }

    /**
     * test readChar() with ascii encoding.
     */
    public final void testReadCharFromStreamAscii()
    {
        InputStream stream = new ByteArrayInputStream(new byte[]{97, 97, 97});
        in = new StreamInImpl(stream, Configuration.ASCII, 8);
        lexer.configuration.inCharEncoding = Configuration.ASCII;
        in.setLexer(lexer);

        char thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
        thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
    }

    /**
     * test readChar() with UTF16 encoding and no BOM.
     */
    public final void testReadCharFromStreamUTF16()
    {
        InputStream stream = new ByteArrayInputStream(new byte[]{00, 97, 00, 97});
        in = new StreamInImpl(stream, Configuration.UTF16BE, 8);
        lexer.configuration.inCharEncoding = Configuration.UTF16BE;
        in.setLexer(lexer);

        char thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
        thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
    }

    /**
     * test readChar() with UTF16 encoding and BE BOM.
     */
    public final void testReadCharFromStreamUTF16WithBOMLE()
    {
        InputStream stream = new ByteArrayInputStream(new byte[]{-1, -2, 97, 00});
        in = new StreamInImpl(stream, Configuration.UTF16, 8);
        lexer.configuration.inCharEncoding = Configuration.UTF16;
        in.setLexer(lexer);

        char thechar = (char) in.readCharFromStream();
        assertEquals(StreamIn.UNICODE_BOM, thechar);
        thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
    }

    /**
     * test readChar() with UTF16 encoding and LE BOM.
     */
    public final void testReadCharFromStreamUTF16WithBOMBE()
    {
        InputStream stream = new ByteArrayInputStream(new byte[]{-2, -1, 00, 97});
        in = new StreamInImpl(stream, Configuration.UTF16, 8);
        lexer.configuration.inCharEncoding = Configuration.UTF16;
        in.setLexer(lexer);

        char thechar = (char) in.readCharFromStream();
        assertEquals(StreamIn.UNICODE_BOM, thechar);
        thechar = (char) in.readCharFromStream();
        assertEquals('a', thechar);
    }

}