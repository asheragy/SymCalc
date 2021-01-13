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

} /* BigDecimalMath */
