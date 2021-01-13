package org.nevec.rjm ;

import java.lang.* ;
import java.security.* ;
import java.util.* ;
import java.math.* ;


/** Complex numbers with BigDecimal real and imaginary components
* @since 2008-10-26
* @author Richard J. Mathar
*/
public class BigComplex
{
        /** real part
        */
        BigDecimal re ;

        /** imaginary part
        */
        BigDecimal im ;

        /** The constant that equals zero
        */
        final static BigComplex ZERO = new BigComplex(BigDecimal.ZERO, BigDecimal.ZERO) ;

        /** Default ctor equivalent to zero.
        */
        public BigComplex()
        {
                re= BigDecimal.ZERO ;
                im= BigDecimal.ZERO ;
        }

        /** ctor with real and imaginary parts
        * @param x real part
        * @param y imaginary part
        * @author Richard J. Mathar
        */
        public BigComplex( BigDecimal x, BigDecimal y)
        {
                re=x ;
                im=y ;
        }

        /** ctor with real part.
        * @param x real part.
        * The imaginary part is set to zero.
        * @author Richard J. Mathar
        */
        public BigComplex( BigDecimal x )
        {
                re=x ;
                im= BigDecimal.ZERO ;
        }

        /** ctor with real and imaginary parts
        * @param x real part
        * @param y imaginary part
        * @author Richard J. Mathar
        */
        public BigComplex( double x, double y)
        {
                re= new BigDecimal(x) ;
                im= new BigDecimal(y) ;
        }

        /** Complex-conjugation
        * @return the complex conjugate of this.
        * @author Richard J. Mathar
        */
        BigComplex conj()
        {
                return new BigComplex(re,im.negate()) ;
        }

        /** The absolute value squared.
        * @return The sum of the squares of real and imaginary parts.
        * This is the square of BigComplex.abs() .
        * @author Richard J. Mathar
        */
        BigDecimal norm()
        {
                return re.multiply(re).add(im.multiply(im)) ;
        }

        /** The absolute value.
        * @param mc The mathematical context (precision) to be used for rounding.
        * @return the square root of the sum of the squares of real and imaginary parts.
        * @since 2008-10-27
        * @author Richard J. Mathar
        */
        BigDecimal abs(MathContext mc)
        {
                return BigDecimalMath.sqrt(norm(),mc) ;
        }

        /** The square root.
        * @param mc The mathematical context (precision) to be used for rounding.
        * @return the square root of the this.
        *   The branch is chosen such that the imaginary part of the result has the
        *   same sign as the imaginary part of this.
        * Tim Ahrendt, <a href="http://dx.doi.org/10.1145/236869.236924">Fast High-precision computation of complex square roots</a>,
        *    ISSAC 1996 p142-149.
        * @since 2008-10-27  
        * @author Richard J. Mathar
        */
        BigComplex sqrt(MathContext mc)
        {
                final BigDecimal half = new BigDecimal("2") ;
                /* compute l=sqrt(re^2+im^2), then u=sqrt((l+re)/2)
                * and v= +- sqrt((l-re)/2 as the new real and imaginary parts.
                */
                final BigDecimal l = abs(mc) ;
                if ( l.compareTo(BigDecimal.ZERO) == 0 )
                        return new BigComplex( BigDecimalMath.scalePrec(BigDecimal.ZERO,mc),
                                                BigDecimalMath.scalePrec(BigDecimal.ZERO,mc) ) ;
                final BigDecimal u = BigDecimalMath.sqrt( l.add(re).divide(half,mc), mc );
                final BigDecimal v = BigDecimalMath.sqrt( l.subtract(re).divide(half,mc), mc );
                if ( im.compareTo(BigDecimal.ZERO)>= 0 )
                        return new BigComplex(u,v) ;
                else
                        return new BigComplex(u,v.negate()) ;
        }

        /** The inverse of this.
        * @param mc The mathematical context (precision) to be used for rounding.
        * @return 1/this
        * @author Richard J. Mathar
        */
        BigComplex inverse(MathContext mc)
        {
                final BigDecimal hyp = norm() ;
                /* 1/(x+iy)= (x-iy)/(x^2+y^2 */
                return new BigComplex( re.divide(hyp,mc), im.divide(hyp,mc).negate() ) ;
        }

        /** Human-readable Fortran-type display
        * @return real and imaginary part in parenthesis, divided by a comma.
        * @author Richard J. Mathar
        */
        public String toString()
        {
                return "("+re.toString()+","+im.toString()+")" ;
        }

        /** Human-readable Fortran-type display
        * @param mc The mathematical context (precision) to be used for rounding.
        * @return real and imaginary part in parenthesis, divided by a comma.
        * @author Richard J. Mathar
        */
        public String toString(MathContext mc)
        {
                return "("+re.round(mc).toString()+","+im.round(mc).toString()+")" ;
        }


} /* BigComplex */
