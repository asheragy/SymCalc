package org.nevec.rjm ;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;


/** BigDecimal special functions.
* <a href="http://arxiv.org/abs/0908.3030">A Java Math.BigDecimal Implementation of Core Mathematical Functions</a>
* @since 2009-05-22
* @author Richard J. Mathar
* <a href="http://apfloat.org/">apfloat</a>
* <a href="http://dfp.sourceforge.net/">dfp</a>
* <a href="http://jscience.org/">JScience</a>
*/
public class BigDecimalMath
{
        /** The square root.
        * @param x the non-negative argument.
        * @param mc The required mathematical precision.
        * @return the square root of the BigDecimal.
        * @since 2008-10-27
        * @author Richard J. Mathar
        */
        static public BigDecimal sqrt(final BigDecimal x, final MathContext mc)
        {
                if ( x.compareTo(BigDecimal.ZERO) < 0 )
                        throw new ArithmeticException("negative argument "+x.toString()+ " of square root") ;
                if ( x.abs().subtract( new BigDecimal(Math.pow(10.,-mc.getPrecision())) ).compareTo(BigDecimal.ZERO) < 0 )
                        return BigDecimalMath.scalePrec(BigDecimal.ZERO,mc) ;
                /* start the computation from a double precision estimate */
                BigDecimal s = new BigDecimal( Math.sqrt(x.doubleValue()) ,mc) ;
                final BigDecimal half = new BigDecimal("2") ;

                /* increase the local accuracy by 2 digits */
                MathContext locmc = new MathContext(mc.getPrecision()+2,mc.getRoundingMode()) ;

                /* relative accuracy requested is 10^(-precision) 
                */
                final double eps = Math.pow(10.0,-mc.getPrecision()) ;
                for (;;)
                {
                        /* s = s -(s/2-x/2s); test correction s-x/s for being
                        * smaller than the precision requested. The relative correction is 1-x/s^2,
                        * (actually half of this, which we use for a little bit of additional protection).
                        */
                        if ( Math.abs(BigDecimal.ONE.subtract(x.divide(s.pow(2,locmc),locmc)).doubleValue()) < eps)
                                break ;

                        // Workaround for the above not working when exponent is less than -238 or whatever max on double is
                        BigDecimal t = s.add(x.divide(s,locmc)).divide(half,locmc) ;
                        if (t.compareTo(s) == 0)
                                break;
                        s = t;

                        /* debugging
                        * System.out.println("itr "+x.round(locmc).toString() + " " + s.round(locmc).toString()) ;
                        */
                }
                return s ;
        } /* BigDecimalMath.sqrt */

        /** Add a BigDecimal and a BigInteger.
        * @param x The left summand
        * @param y The right summand
        * @return The sum x+y.
        * @since 2012-03-02
        * @author Richard J. Mathar
        */
        static public BigDecimal add(final BigDecimal x, final BigInteger y)
        {
                return x.add(new BigDecimal(y)) ;
        } /* add */


        /** Add and round according to the larger of the two ulp's.
        * @param x The left summand
        * @param y The right summand
        * @return The sum x+y.
        * @since 2009-07-30
        * @author Richard J. Mathar
        */
        static public BigDecimal addRound(final BigDecimal x, final BigDecimal y)
        {
                BigDecimal resul = x.add(y) ;
                /* The estimation of the absolute error in the result is |err(y)|+|err(x)| 
                */
                double errR = Math.abs( y.ulp().doubleValue()/2. ) + Math.abs( x.ulp().doubleValue()/2. ) ;
                MathContext mc = new MathContext( err2prec(resul.doubleValue(),errR) ) ;
                return resul.round(mc) ;
        } /* addRound */

        /** Divide and round.
        * @param n The numerator
        * @param x The denominator
        * @return the divided n/x
        * @since 2009-08-05
        * @author Richard J. Mathar
        */
        static public BigDecimal divideRound(final BigInteger n, final BigDecimal x)
        {
                /* The estimation of the relative error in the result is |err(x)/x| 
                */
                MathContext mc = new MathContext( x.precision() ) ;
                return new BigDecimal(n).divide(x,mc) ;
        } /* divideRound */

        /** Append decimal zeros to the value. This returns a value which appears to have
        * a higher precision than the input.
        * @param x The input value
        * @param d The (positive) value of zeros to be added as least significant digits.
        * @return The same value as the input but with increased (pseudo) precision.
        * @author Richard J. Mathar
        */
        static public BigDecimal scalePrec(final BigDecimal x, int d)
        {
                return x.setScale(d+x.scale()) ;
        }

        /** Boost the precision by appending decimal zeros to the value. This returns a value which appears to have
        * a higher precision than the input.
        * @param x The input value
        * @param mc The requirement on the minimum precision on return.
        * @return The same value as the input but with increased (pseudo) precision.
        * @author Richard J. Mathar
        */
        static public BigDecimal scalePrec(final BigDecimal x, final MathContext mc)
        {
                final int diffPr = mc.getPrecision() - x.precision() ;
                if ( diffPr > 0 )
                        return scalePrec(x, diffPr) ;
                else
                        return x ;
        } /* BigDecimalMath.scalePrec */

        /** Convert an absolute error to a precision.
        * @param x The value of the variable
        * @param xerr The absolute error in the variable
        * @return The number of valid digits in x.
        *    The value is rounded down, and on the pessimistic side for that reason.
        * @since 2009-06-25
        * @author Richard J. Mathar
        */
        static public int err2prec(BigDecimal x, BigDecimal xerr)
        {
                return err2prec( xerr.divide(x,MathContext.DECIMAL64).doubleValue() );
        }

        /** Convert an absolute error to a precision.
        * @param x The value of the variable
        *    The value returned depends only on the absolute value, not on the sign.
        * @param xerr The absolute error in the variable
        *    The value returned depends only on the absolute value, not on the sign.
        * @return The number of valid digits in x.
        *    Derived from the representation x+- xerr, as if the error was represented
        *    in a "half width" (half of the error bar) form.
        *    The value is rounded down, and on the pessimistic side for that reason.
        * @since 2009-05-30
        * @author Richard J. Mathar
        */
        static public int err2prec(double x, double xerr)
        {
                /* Example: an error of xerr=+-0.5 at x=100 represents 100+-0.5 with
                * a precision = 3 (digits).
                */
                return 1+(int)(Math.log10(Math.abs(0.5*x/xerr) ) );
        }

        /** Convert a relative error to a precision.
        * @param xerr The relative error in the variable.
        *    The value returned depends only on the absolute value, not on the sign.
        * @return The number of valid digits in x.
        *    The value is rounded down, and on the pessimistic side for that reason.
        * @since 2009-08-05
        * @author Richard J. Mathar
        */
        static public int err2prec(double xerr)
        {
                /* Example: an error of xerr=+-0.5 a precision of 1 (digit), an error of
                * +-0.05 a precision of 2 (digits)
                */
                return 1+(int)(Math.log10(Math.abs(0.5/xerr) ) );
        }

} /* BigDecimalMath */
