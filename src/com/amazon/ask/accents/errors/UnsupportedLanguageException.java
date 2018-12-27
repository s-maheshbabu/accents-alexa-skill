package com.amazon.ask.accents.errors;

/**
 * Represents exception when an unknown/unsupported language is encountered.
 */
public class UnsupportedLanguageException extends RuntimeException
{

    private static final long serialVersionUID = 1L;

    public UnsupportedLanguageException(final String message)
    {
        super(message);
    }

    public UnsupportedLanguageException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

}