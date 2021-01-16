package org.nevec.rjm ;

import java.lang.* ;
import java.security.* ;
import java.util.* ;
import java.math.* ;


/** BigInteger special functions and Number theory.
* @since 2009-08-06
* @author Richard J. Mathar
*/
public class BigIntegerMath
{
        /** Evaluate floor(sqrt(n)).
        * @param n The non-negative argument.
        *  Arguments less than zero throw an ArithmeticException.
        * @return The integer square root, the square root rounded down.
        * @since 2011-02-12
        * @author Richard J. Mathar
        */
        static public BigInteger isqrt(final BigInteger n)
        {
                if ( n.compareTo(BigInteger.ZERO) < 0 )
                        throw new ArithmeticException("Negative argument "+ n.toString()) ;
                /* Start with an estimate from a floating point reduction.
                */
                BigInteger x  ;
                final int bl = n.bitLength() ;
                if ( bl > 120)
                        x = n.shiftRight(bl/2-1) ;
                else
                {
                        final double resul= Math.sqrt(n.doubleValue()) ;
                        x = new BigInteger(""+Math.round(resul)) ;
                }

                final BigInteger two = BigInteger.valueOf(2L) ;
                while ( true)
                {
                        /* check whether the result is accurate, x^2 =n
                        */
                        BigInteger x2 = x.pow(2) ;
                        BigInteger xplus2 = x.add(BigInteger.ONE).pow(2) ;
                        if ( x2.compareTo(n) <= 0 && xplus2.compareTo(n) > 0)
                                return x ;
                        xplus2 = xplus2.subtract(x.shiftLeft(2)) ;
                        if ( xplus2.compareTo(n) <= 0 && x2.compareTo(n) > 0)
                                return x.subtract(BigInteger.ONE) ;
                        /* Newton algorithm. This correction is on the
                        * low side caused by the integer divisions. So the value required
                        * may end up by one unit too large by the bare algorithm, and this
                        * is caught above by comparing x^2, (x+-1)^2 with n.
                        */
                        xplus2 = x2.subtract(n).divide(x).divide(two) ;
                        x = x.subtract(xplus2) ;
                }
        } /* isqrt */

        /** Evaluate floor(root[n](x)).
        * @param x The non-negative argument.
        *  Arguments less than zero throw an ArithmeticException.
        * @param n The positive inverse power.
        * @return The integer n-th root of x, rounded down.
        * @since 2012-11-29
        * @author Richard J. Mathar
        */
        static public BigInteger iroot(final BigInteger x, final int n)
        {
                if ( x.compareTo(BigInteger.ZERO) < 0 )
                        throw new ArithmeticException("Negative argument "+ x.toString()) ;
                if ( n < 1 )
                        throw new ArithmeticException("Non-positive argument "+ n) ;
                /* Start with an estimate from a floating point reduction.
                */
                BigInteger r  ;
                final BigInteger nBig = BigInteger.valueOf((long)n)  ;
                final int bl = x.bitLength() ;
                if ( bl > 120)
                        r = x.shiftRight(bl/n-1) ;
                else
                {
                        final double resul= Math.pow(x.doubleValue(),1.0/n) ;
                        r = new BigInteger(""+Math.round(resul)) ;
                }

                while ( true)
                {
                        /* check whether the result is accurate, r^n =x
                        */
                        BigInteger r2 = r.pow(n) ;
                        BigInteger rplus2 = r.add(BigInteger.ONE).pow(n) ;
                        if ( r2.compareTo(x) <= 0 && rplus2.compareTo(x) > 0)
                                return r ;
                        rplus2 = r.subtract(BigInteger.ONE).pow(n) ;
                        if ( rplus2.compareTo(x) <= 0 && r2.compareTo(x) > 0)
                                return r.subtract(BigInteger.ONE) ;
                        rplus2 = r2.subtract(x).divide(r.pow(n-1)).divide(nBig) ;
                        r = r.subtract(rplus2) ;
                }
        } /* iroot */


} /* BigIntegerMath */
