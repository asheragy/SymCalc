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
        /** Natural logarithm of 2.
        * http://www.worldwideschool.org/library/books/sci/math/MiscellaneousMathematicalConstants/chap58.html
        */
        static BigDecimal LOG2 = new BigDecimal("0.693147180559945309417232121458176568075"+
"50013436025525412068000949339362196969471560586332699641868754200148102057068573"+
"368552023575813055703267075163507596193072757082837143519030703862389167347112335"+
"011536449795523912047517268157493206515552473413952588295045300709532636664265410"+
"423915781495204374043038550080194417064167151864471283996817178454695702627163106"+
"454615025720740248163777338963855069526066834113727387372292895649354702576265209"+
"885969320196505855476470330679365443254763274495125040606943814710468994650622016"+
"772042452452961268794654619316517468139267250410380254625965686914419287160829380"+
"317271436778265487756648508567407764845146443994046142260319309673540257444607030"+
"809608504748663852313818167675143866747664789088143714198549423151997354880375165"+
"861275352916610007105355824987941472950929311389715599820565439287170007218085761"+
"025236889213244971389320378439353088774825970171559107088236836275898425891853530"+
"243634214367061189236789192372314672321720534016492568727477823445353476481149418"+
"642386776774406069562657379600867076257199184734022651462837904883062033061144630"+
"073719489002743643965002580936519443041191150608094879306786515887090060520346842"+
"973619384128965255653968602219412292420757432175748909770675268711581705113700915"+
"894266547859596489065305846025866838294002283300538207400567705304678700184162404"+
"418833232798386349001563121889560650553151272199398332030751408426091479001265168"+
"243443893572472788205486271552741877243002489794540196187233980860831664811490930"+
"667519339312890431641370681397776498176974868903887789991296503619270710889264105"+
"230924783917373501229842420499568935992206602204654941510613");

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

        /** The square root.
        * @param x the non-negative argument.
        * @return the square root of the BigDecimal rounded to the precision implied by x.
        * @since 2009-06-25
        * @author Richard J. Mathar
        */
        static public BigDecimal sqrt(final BigDecimal x)
        {
                if ( x.compareTo(BigDecimal.ZERO) < 0 )
                        throw new ArithmeticException("negative argument "+x.toString()+ " of square root") ;

                return root(2,x) ;
        } /* BigDecimalMath.sqrt */

        /** The integer root.
        * @param n the positive argument.
        * @param x the non-negative argument.
        * @return The n-th root of the BigDecimal rounded to the precision implied by x, x^(1/n).
        * @since 2009-07-30
        * @author Richard J. Mathar
        */
        static public BigDecimal root(final int n, final BigDecimal x)
        {
                if ( x.compareTo(BigDecimal.ZERO) < 0 )
                        throw new ArithmeticException("negative argument "+x.toString()+ " of root") ;
                if ( n<= 0 )
                        throw new ArithmeticException("negative power "+ n + " of root") ;

                if ( n == 1 )
                        return x ;

                /* start the computation from a double precision estimate */
                BigDecimal s = new BigDecimal( Math.pow(x.doubleValue(),1.0/n) ) ;

                /* this creates nth with nominal precision of 1 digit
                */
                final BigDecimal nth = new BigDecimal(n) ;

                /* Specify an internal accuracy within the loop which is
                * slightly larger than what is demanded by 'eps' below.
                */
                final BigDecimal xhighpr = scalePrec(x,2) ;
                MathContext mc = new MathContext( 2+x.precision() ) ;

                /* Relative accuracy of the result is eps.
                */
                final double eps = x.ulp().doubleValue()/(2*n*x.doubleValue()) ;
                for (;;)
                {
                        /* s = s -(s/n-x/n/s^(n-1)) = s-(s-x/s^(n-1))/n; test correction s/n-x/s for being
                        * smaller than the precision requested. The relative correction is (1-x/s^n)/n,
                        */
                        BigDecimal c = xhighpr.divide( s.pow(n-1),mc ) ;
                        c = s.subtract(c) ;
                        MathContext locmc = new MathContext( c.precision() ) ;
                        c = c.divide(nth,locmc) ;
                        s = s. subtract(c) ;

                        double cd = c.doubleValue();
                        double sd = s.doubleValue();

                        if ( Math.abs( c.doubleValue()/s.doubleValue()) < eps)
                                break ;

                        if (c.scale() - c.precision() == mc.getPrecision())
                                return s;
                }
                return s.round(new MathContext( err2prec(eps)) ) ;
        } /* BigDecimalMath.root */

        /** A suggestion for the maximum numter of terms in the Taylor expansion of the exponential.
        */
        static private int TAYLOR_NTERM = 8 ;

        /** The exponential function.
        * @param x the argument.
        * @return exp(x).
        * The precision of the result is implicitly defined by the precision in the argument.
        * In particular this means that "Invalid Operation" errors are thrown if catastrophic
        * cancellation of digits causes the result to have no valid digits left.
        * @since 2009-05-29
        * @author Richard J. Mathar
        */
        static public BigDecimal exp(BigDecimal x)
        {
                /* To calculate the value if x is negative, use exp(-x) = 1/exp(x)
                */
                if ( x.compareTo(BigDecimal.ZERO) < 0 )
                {
                        final BigDecimal invx = exp(x.negate() ) ;
                        /* Relative error in inverse of invx is the same as the relative errror in invx.
                        * This is used to define the precision of the result.
                        */
                        MathContext mc = new MathContext( invx.precision() ) ;
                        return BigDecimal.ONE.divide( invx, mc ) ;
                }
                else if ( x.compareTo(BigDecimal.ZERO) == 0 )
                {
                        /* recover the valid number of digits from x.ulp(), if x hits the
                        * zero. The x.precision() is 1 then, and does not provide this information.
                        */
                        return scalePrec(BigDecimal.ONE, -(int)(Math.log10( x.ulp().doubleValue() )) ) ;
                }
                else
                {
                        /* Push the number in the Taylor expansion down to a small
                        * value where TAYLOR_NTERM terms will do. If x<1, the n-th term is of the order
                        * x^n/n!, and equal to both the absolute and relative error of the result
                        * since the result is close to 1. The x.ulp() sets the relative and absolute error
                        * of the result, as estimated from the first Taylor term.
                        * We want x^TAYLOR_NTERM/TAYLOR_NTERM! < x.ulp, which is guaranteed if
                        * x^TAYLOR_NTERM < TAYLOR_NTERM*(TAYLOR_NTERM-1)*...*x.ulp.
                        */
                        final double xDbl = x.doubleValue() ;
                        final double xUlpDbl = x.ulp().doubleValue() ;
                        if ( Math.pow(xDbl,TAYLOR_NTERM) < TAYLOR_NTERM*(TAYLOR_NTERM-1.0)*(TAYLOR_NTERM-2.0)*xUlpDbl ) 
                        {
                                /* Add TAYLOR_NTERM terms of the Taylor expansion (Euler's sum formula)
                                */
                                BigDecimal resul = BigDecimal.ONE ;

                                /* x^i */
                                BigDecimal xpowi = BigDecimal.ONE ;

                                /* i factorial */
                                BigInteger ifac = BigInteger.ONE ;

                                /* TAYLOR_NTERM terms to be added means we move x.ulp() to the right
                                * for each power of 10 in TAYLOR_NTERM, so the addition won't add noise beyond
                                * what's already in x.
                                */
                                MathContext mcTay = new MathContext( err2prec(1.,xUlpDbl/TAYLOR_NTERM) ) ;
                                for(int i=1 ; i <= TAYLOR_NTERM ; i++)
                                {
                                        ifac = ifac.multiply(new BigInteger(""+i) ) ;
                                        xpowi = xpowi.multiply(x) ;
                                        final BigDecimal c= xpowi.divide(new BigDecimal(ifac),mcTay)  ;
                                        resul = resul.add(c) ;
                                        if ( Math.abs(xpowi.doubleValue()) < i && Math.abs(c.doubleValue()) < 0.5* xUlpDbl )
                                                break;
                                }
                                /* exp(x+deltax) = exp(x)(1+deltax) if deltax is <<1. So the relative error
                                * in the result equals the absolute error in the argument.
                                */
                                MathContext mc = new MathContext( err2prec(xUlpDbl/2.) ) ;
                                return resul.round(mc) ;
                        }
                        else
                        {
                                /* Compute exp(x) = (exp(0.1*x))^10. Division by 10 does not lead
                                * to loss of accuracy.
                                */
                                int exSc = (int) ( 1.0-Math.log10( TAYLOR_NTERM*(TAYLOR_NTERM-1.0)*(TAYLOR_NTERM-2.0)*xUlpDbl
                                                        /Math.pow(xDbl,TAYLOR_NTERM) ) / ( TAYLOR_NTERM-1.0) ) ; 
                                BigDecimal xby10 = x.scaleByPowerOfTen(-exSc) ;
                                BigDecimal expxby10 = exp(xby10) ;

                                /* Final powering by 10 means that the relative error of the result
                                * is 10 times the relative error of the base (First order binomial expansion).
                                * This looses one digit.
                                */
                                MathContext mc = new MathContext( expxby10.precision()-exSc ) ;
                                /* Rescaling the powers of 10 is done in chunks of a maximum of 8 to avoid an invalid operation
                                * response by the BigDecimal.pow library or integer overflow.
                                */
                                while ( exSc > 0 )
                                {
                                        int exsub = Math.min(8,exSc) ;
                                        exSc -= exsub ;
                                        MathContext mctmp = new MathContext( expxby10.precision()-exsub+2 ) ;
                                        int pex = 1 ;
                                        while ( exsub-- > 0 )
                                                pex *= 10 ;
                                        expxby10 = expxby10.pow(pex,mctmp) ;
                                }
                                return expxby10.round(mc) ;
                        }
                }
        } /* BigDecimalMath.exp */

        /** The natural logarithm.
        * @param x the argument.
        * @return ln(x).
        * The precision of the result is implicitly defined by the precision in the argument.
        * @since 2009-05-29
        * @author Richard J. Mathar
        */
        static public BigDecimal log(BigDecimal x)
        {
                /* the value is undefined if x is negative.
                */
                if ( x.compareTo(BigDecimal.ZERO) < 0 )
                        throw new ArithmeticException("Cannot take log of negative "+ x.toString() ) ;
                else if ( x.compareTo(BigDecimal.ONE) == 0 )
                {
                        /* log 1. = 0. */
                        return scalePrec(BigDecimal.ZERO, x.precision()-1) ;
                }
                else if ( Math.abs(x.doubleValue()-1.0) <= 0.3 )
                {
                        /* The standard Taylor series around x=1, z=0, z=x-1. Abramowitz-Stegun 4.124.
                        * The absolute error is err(z)/(1+z) = err(x)/x.
                        */
                        BigDecimal z = scalePrec(x.subtract(BigDecimal.ONE),2) ;
                        BigDecimal zpown = z ;
                        double eps = 0.5*x.ulp().doubleValue()/Math.abs(x.doubleValue()) ;
                        BigDecimal resul = z ;
                        for(int k= 2;; k++)
                        {
                                zpown = multiplyRound(zpown,z) ;
                                BigDecimal c = divideRound(zpown,k) ;
                                if ( k % 2 == 0)
                                        resul = resul.subtract(c) ;
                                else
                                        resul = resul.add(c) ;
                                if ( Math.abs(c.doubleValue()) < eps)
                                        break;
                        }
                        MathContext mc = new MathContext( err2prec(resul.doubleValue(),eps) ) ;
                        return resul.round(mc) ;
                }
                else
                {
                        final double xDbl = x.doubleValue() ;
                        final double xUlpDbl = x.ulp().doubleValue() ;

                        /* Map log(x) = log root[r](x)^r = r*log( root[r](x)) with the aim
                        * to move roor[r](x) near to 1.2 (that is, below the 0.3 appearing above), where log(1.2) is roughly 0.2.
                        */
                        int r = (int) (Math.log(xDbl)/0.2) ;

                        /* Since the actual requirement is a function of the value 0.3 appearing above,
                        * we avoid the hypothetical case of endless recurrence by ensuring that r >= 2.
                        */
                        r = Math.max(2,r) ;

                        /* Compute r-th root with 2 additional digits of precision
                        */
                        BigDecimal xhighpr = scalePrec(x,2) ;
                        BigDecimal resul = root(r,xhighpr) ;
                        resul = log(resul).multiply(new BigDecimal(r)) ;

                        /* error propagation: log(x+errx) = log(x)+errx/x, so the absolute error
                        * in the result equals the relative error in the input, xUlpDbl/xDbl .
                        */
                        MathContext mc = new MathContext( err2prec(resul.doubleValue(),xUlpDbl/xDbl) ) ;
                        return resul.round(mc) ;
                }
        } /* BigDecimalMath.log */

        /** The natural logarithm.
        * @param n The main argument, a strictly positive integer.
        * @param mc The requirements on the precision.
        * @return ln(n).
        * @since 2009-08-08
        * @author Richard J. Mathar
        */
        static public BigDecimal log(int n, final MathContext mc)
        {
                /* the value is undefined if x is negative.
                */
                if ( n <= 0 )
                        throw new ArithmeticException("Cannot take log of negative "+ n ) ;
                else if ( n == 1)
                        return BigDecimal.ZERO ;
                else if ( n == 2)
                {
                        if ( mc.getPrecision() < LOG2.precision() )
                                return LOG2.round(mc) ;
                        else
                        {
                                /* Broadhurst <a href="http://arxiv.org/abs/math/9803067">arXiv:math/9803067</a>
                                * Error propagation: the error in log(2) is twice the error in S(2,-5,...).
                                */
                                int[] a = {2,-5,-2,-7,-2,-5,2,-3} ; 
                                BigDecimal S = broadhurstBBP(2,1,a, new MathContext(1+mc.getPrecision()) ) ;
                                S = S.multiply(new BigDecimal(8)) ;
                                S = sqrt(divideRound(S,3)) ;
                                return S.round(mc) ;
                        }
                }
                else if ( n == 3)
                {
                        /* summation of a series roughly proportional to (7/500)^k. Estimate count
                        * of terms to estimate the precision (drop the favorable additional
                        * 1/k here): 0.013^k <= 10^(-precision), so k*log10(0.013) <= -precision
                        * so k>= precision/1.87.
                        */
                        int kmax = (int)(mc.getPrecision()/1.87) ;
                        MathContext mcloc = new MathContext( mc.getPrecision()+ 1+(int)(Math.log10(kmax*0.693/1.098)) ) ;
                        BigDecimal log3 = multiplyRound( log(2,mcloc),19 ) ;

                        /* log3 is roughly 1, so absolute and relative error are the same. The
                        * result will be divided by 12, so a conservative error is the one
                        * already found in mc
                        */
                        double eps = prec2err(1.098,mc.getPrecision() )/kmax ;
                        Rational r = new Rational(7153,524288) ;
                        Rational pk = new Rational(7153,524288) ;
                        for(int k=1; ; k++)
                        {
                                Rational tmp = pk.divide(k) ;
                                if ( tmp.doubleValue() < eps)
                                        break ;

                                /* how many digits of tmp do we need in the sum?
                                */
                                mcloc = new MathContext( err2prec(tmp.doubleValue(),eps) ) ;
                                BigDecimal c = pk.divide(k).BigDecimalValue(mcloc) ;
                                if ( k % 2 != 0)
                                        log3 = log3.add(c) ;
                                else
                                        log3 = log3.subtract(c) ;
                                pk = pk.multiply(r) ;
                        }
                        log3 = divideRound( log3,12 ) ;
                        return log3.round(mc) ;
                }
                else if ( n == 5)
                {
                        /* summation of a series roughly proportional to (7/160)^k. Estimate count
                        * of terms to estimate the precision (drop the favorable additional
                        * 1/k here): 0.046^k <= 10^(-precision), so k*log10(0.046) <= -precision
                        * so k>= precision/1.33.
                        */
                        int kmax = (int)(mc.getPrecision()/1.33) ;
                        MathContext mcloc = new MathContext( mc.getPrecision()+ 1+(int)(Math.log10(kmax*0.693/1.609)) ) ;
                        BigDecimal log5 = multiplyRound( log(2,mcloc),14 ) ;

                        /* log5 is roughly 1.6, so absolute and relative error are the same. The
                        * result will be divided by 6, so a conservative error is the one
                        * already found in mc
                        */
                        double eps = prec2err(1.6,mc.getPrecision() )/kmax ;
                        Rational r = new Rational(759,16384) ;
                        Rational pk = new Rational(759,16384) ;
                        for(int k=1; ; k++)
                        {
                                Rational tmp = pk.divide(k) ;
                                if ( tmp.doubleValue() < eps)
                                        break ;

                                /* how many digits of tmp do we need in the sum?
                                */
                                mcloc = new MathContext( err2prec(tmp.doubleValue(),eps) ) ;
                                BigDecimal c = pk.divide(k).BigDecimalValue(mcloc) ;
                                log5 = log5.subtract(c) ;
                                pk = pk.multiply(r) ;
                        }
                        log5 = divideRound( log5,6 ) ;
                        return log5.round(mc) ;
                }
                else if ( n == 7)
                {
                        /* summation of a series roughly proportional to (1/8)^k. Estimate count
                        * of terms to estimate the precision (drop the favorable additional
                        * 1/k here): 0.125^k <= 10^(-precision), so k*log10(0.125) <= -precision
                        * so k>= precision/0.903.
                        */
                        int kmax = (int)(mc.getPrecision()/0.903) ;
                        MathContext mcloc = new MathContext( mc.getPrecision()+ 1+(int)(Math.log10(kmax*3*0.693/1.098)) ) ;
                        BigDecimal log7 = multiplyRound( log(2,mcloc),3 ) ;

                        /* log7 is roughly 1.9, so absolute and relative error are the same.
                        */
                        double eps = prec2err(1.9,mc.getPrecision() )/kmax ;
                        Rational r = new Rational(1,8) ;
                        Rational pk = new Rational(1,8) ;
                        for(int k=1; ; k++)
                        {
                                Rational tmp = pk.divide(k) ;
                                if ( tmp.doubleValue() < eps)
                                        break ;

                                /* how many digits of tmp do we need in the sum?
                                */
                                mcloc = new MathContext( err2prec(tmp.doubleValue(),eps) ) ;
                                BigDecimal c = pk.divide(k).BigDecimalValue(mcloc) ;
                                log7 = log7.subtract(c) ;
                                pk = pk.multiply(r) ;
                        }
                        return log7.round(mc) ;

                }

                else
                {
                        /* At this point one could either forward to the log(BigDecimal) signature (implemented)
                        * or decompose n into Ifactors and use an implemenation of all the prime bases.
                        * Estimate of the result; convert the mc argument to an  absolute error eps
                        * log(n+errn) = log(n)+errn/n = log(n)+eps
                        */
                        double res = Math.log((double)n) ;
                        double eps = prec2err(res,mc.getPrecision() ) ;
                        /* errn = eps*n, convert absolute error in result to requirement on absolute error in input
                        */
                        eps *= n ;
                        /* Convert this absolute requirement of error in n to a relative error in n
                        */
                        final MathContext mcloc = new MathContext( 1+err2prec((double)n,eps ) ) ;
                        /* Padd n with a number of zeros to trigger the required accuracy in
                        * the standard signature method
                        */
                        BigDecimal nb = scalePrec(new BigDecimal(n),mcloc) ;
                        return log(nb) ;
                }
        } /* log */

        /** Broadhurst ladder sequence.
        * @param n
        * @param p
        * @param mc Specification of the accuracy of the result
        * @return S_(n,p)(a)
        * @since 2009-08-09
        * <a href="http://arxiv.org/abs/math/9803067">arXiv:math/9803067</a>
        * @author Richard J. Mathar
        */
        static protected BigDecimal broadhurstBBP(final int n, final int p, final int a[], MathContext mc)
        {
                /* Explore the actual magnitude of the result first with a quick estimate.
                */
                double x = 0.0 ;
                for(int k=1; k < 10 ; k++)
                        x += a[ (k-1) % 8]/Math.pow(2., p*(k+1)/2)/Math.pow((double)k,n) ;

                /* Convert the relative precision and estimate of the result into an absolute precision.
                */
                double eps = prec2err(x,mc.getPrecision()) ;

                /* Divide this through the number of terms in the sum to account for error accumulation
                * The divisor 2^(p(k+1)/2) means that on the average each 8th term in k has shrunk by
                * relative to the 8th predecessor by 1/2^(4p).  1/2^(4pc) = 10^(-precision) with c the 8term
                * cycles yields c=log_2( 10^precision)/4p = 3.3*precision/4p  with k=8c
                */
                int kmax= (int)(6.6*mc.getPrecision()/p) ;

                /* Now eps is the absolute error in each term */
                eps /= kmax ;
                BigDecimal res = BigDecimal.ZERO ;
                for(int c =0 ; ; c++)
                {
                        Rational r = new Rational() ;
                        for (int k=0; k < 8 ; k++)
                        {
                                Rational tmp = new Rational(new BigInteger(""+a[k]),(new BigInteger(""+(1+8*c+k))).pow(n)) ;
                                /* floor( (pk+p)/2)
                                */
                                int pk1h = p*(2+8*c+k)/2 ;
                                tmp = tmp.divide( BigInteger.ONE.shiftLeft(pk1h) ) ;
                                r = r.add(tmp) ;
                        }
        
                        if ( Math.abs(r.doubleValue()) < eps)
                                break;
                        MathContext mcloc = new MathContext( 1+err2prec(r.doubleValue(),eps) ) ;
                        res = res.add( r.BigDecimalValue(mcloc) ) ;
                }
                return res.round(mc) ;
        } /* broadhurstBBP */

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

        /** Multiply and round.
        * @param x The left factor.
        * @param y The right factor.
        * @return The product x*y.
        * @since 2009-07-30
        * @author Richard J. Mathar
        */
        static public BigDecimal multiplyRound(final BigDecimal x, final BigDecimal y)
        {
                BigDecimal resul = x.multiply(y) ;
                /* The estimation of the relative error in the result is the sum of the relative
                * errors |err(y)/y|+|err(x)/x| 
                */
                MathContext mc = new MathContext( Math.min(x.precision(),y.precision()) ) ;
                return resul.round(mc) ;
        } /* multiplyRound */

        /** Multiply and round.
        * @param x The left factor.
        * @param n The right factor.
        * @return The product x*n.
        * @since 2009-07-30
        * @author Richard J. Mathar
        */
        static public BigDecimal multiplyRound(final BigDecimal x, final int n)
        {
                BigDecimal resul = x.multiply(new BigDecimal(n)) ;
                /* The estimation of the absolute error in the result is |n*err(x)|
                */
                MathContext mc = new MathContext( n != 0 ? x.precision(): 0 ) ;
                return resul.round(mc) ;
        }

        /** Divide and round.
        * @param x The numerator
        * @param n The denominator
        * @return the divided x/n
        * @since 2009-07-30
        * @author Richard J. Mathar
        */
        static public BigDecimal divideRound(final BigDecimal x, final int n)
        {
                /* The estimation of the relative error in the result is |err(x)/x| 
                */
                MathContext mc = new MathContext( x.precision() ) ;
                return x.divide(new BigDecimal(n),mc) ;
        }

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

        /** Convert a precision (relative error) to an absolute error.
        *    The is the inverse functionality of err2prec().
        * @param x The value of the variable
        *    The value returned depends only on the absolute value, not on the sign.
        * @param prec The number of valid digits of the variable.
        * @return the absolute error in x.
        *    Derived from the an accuracy of one half of the ulp.
        * @since 2009-08-09
        * @author Richard J. Mathar
        */
        static public double prec2err(final double x, final int prec)
        {
                return 5.*Math.abs(x)*Math.pow(10.,-prec) ;
        }

} /* BigDecimalMath */
