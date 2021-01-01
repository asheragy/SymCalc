package org.nevec.rjm ;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.security.ProviderException;


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
        /** Euler's constant Pi.
        * http://www.cs.arizona.edu/icon/oddsends/pi.htm
        */
        static BigDecimal PI = new BigDecimal("3.14159265358979323846264338327950288419716939937510582097494459230781640628620"+
"899862803482534211706798214808651328230664709384460955058223172535940812848111"+
"745028410270193852110555964462294895493038196442881097566593344612847564823378"+
"678316527120190914564856692346034861045432664821339360726024914127372458700660"+
"631558817488152092096282925409171536436789259036001133053054882046652138414695"+
"194151160943305727036575959195309218611738193261179310511854807446237996274956"+
"735188575272489122793818301194912983367336244065664308602139494639522473719070"+
"217986094370277053921717629317675238467481846766940513200056812714526356082778"+
"577134275778960917363717872146844090122495343014654958537105079227968925892354"+
"201995611212902196086403441815981362977477130996051870721134999999837297804995"+
"105973173281609631859502445945534690830264252230825334468503526193118817101000"+
"313783875288658753320838142061717766914730359825349042875546873115956286388235"+
"378759375195778185778053217122680661300192787661119590921642019893809525720106"+
"548586327886593615338182796823030195203530185296899577362259941389124972177528"+
"347913151557485724245415069595082953311686172785588907509838175463746493931925"+
"506040092770167113900984882401285836160356370766010471018194295559619894676783"+
"744944825537977472684710404753464620804668425906949129331367702898915210475216"+
"205696602405803815019351125338243003558764024749647326391419927260426992279678"+
"235478163600934172164121992458631503028618297455570674983850549458858692699569"+
"092721079750930295532116534498720275596023648066549911988183479775356636980742"+
"654252786255181841757467289097777279380008164706001614524919217321721477235014") ;

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

        /** Euler's constant.
        * @param mc The required precision of the result.
        * @return 3.14159...
        * @since 2009-05-29
        * @author Richard J. Mathar
        */
        static public BigDecimal pi(final MathContext mc)
        {
                /* look it up if possible */
                if ( mc.getPrecision() < PI.precision() )
                        return PI.round(mc) ;
                else
                {
                        /* Broadhurst <a href="http://arxiv.org/abs/math/9803067">arXiv:math/9803067</a>
                        */
                        int[] a = {1,0,0,-1,-1,-1,0,0} ; 
                        BigDecimal S = broadhurstBBP(1,1,a,mc) ;
                        return multiplyRound(S,8) ;
                }
        } /* BigDecimalMath.pi */

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

        /** Raise to an integer power and round.
        * @param x The base.
        * @param n The exponent.
        * @return x^n.
        * @since 2009-08-13
        * @since 2010-05-26 handle also cases where n is less than zero.
        * @author Richard J. Mathar
        */
        static public BigDecimal powRound(final BigDecimal x, final int n)
        {
                /** Special cases: x^1=x and x^0 = 1
                */
                if ( n == 1 )
                        return x;
                else if ( n == 0 )
                        return BigDecimal.ONE ;
                else
                {
                        /* The relative error in the result is n times the relative error in the input.
                        * The estimation is slightly optimistic due to the integer rounding of the logarithm.
                        * Since the standard BigDecimal.pow can only handle positive n, we split the algorithm.
                        */
                        MathContext mc = new MathContext( x.precision() - (int)Math.log10((double)(Math.abs(n))) ) ;
                        if ( n > 0 )
                                return x.pow(n,mc) ;
                        else
                                return BigDecimal.ONE.divide( x.pow(-n),mc) ;
                }
        } /* BigDecimalMath.powRound */

        /** Riemann zeta function.
        * @param n The positive integer argument.
        * @param mc Specification of the accuracy of the result.
        * @return zeta(n).
        * @since 2009-08-05
        * @author Richard J. Mathar
        */
        static public BigDecimal zeta(final int n, final MathContext mc)
        {
                if( n <= 0 )
                        throw new ProviderException("Not implemented: zeta at negative argument "+n) ;
                if( n == 1 )
                        throw new ArithmeticException("Pole at zeta(1) ") ;

                if( n % 2 == 0 )
                {
                        /* Even indices. Abramowitz-Stegun 23.2.16. Start with 2^(n-1)*B(n)/n!
                        */
                        Rational b = (new Bernoulli()).at(n).abs() ;
                        b = b.divide((new Factorial()).at(n)) ;
                        b = b.multiply( BigInteger.ONE.shiftLeft(n-1) );

                        /* to be multiplied by pi^n. Absolute error in the result of pi^n is n times
                        * error in pi times pi^(n-1). Relative error is n*error(pi)/pi, requested by mc.
                        * Need one more digit in pi if n=10, two digits if n=100 etc, and add one extra digit.
                        */
                        MathContext mcpi = new MathContext( mc.getPrecision() + (int)(Math.log10(10.0*n)) ) ;
                        final BigDecimal piton = pi(mcpi).pow(n,mc) ;
                        return multiplyRound( piton, b) ;
                }
                else if ( n == 3)
                {
                        /* Broadhurst BBP <a href="http://arxiv.org/abs/math/9803067">arXiv:math/9803067</a>
                        * Error propagation: S31 is roughly 0.087, S33 roughly 0.131
                        */
                        int[] a31 = {1,-7,-1,10,-1,-7,1,0} ; 
                        int[] a33 = {1,1,-1,-2,-1,1,1,0} ; 
                        BigDecimal S31 = broadhurstBBP(3,1,a31,mc) ;
                        BigDecimal S33 = broadhurstBBP(3,3,a33,mc) ;
                        S31 = S31.multiply(new BigDecimal(48)) ;
                        S33 = S33.multiply(new BigDecimal(32)) ;
                        return S31.add(S33).divide(new BigDecimal(7),mc) ;
                }
                else if ( n == 5)
                {
                        /* Broadhurst BBP <a href=http://arxiv.org/abs/math/9803067">arXiv:math/9803067</a>
                        * Error propagation: S51 is roughly -11.15, S53 roughly 22.165, S55 is roughly 0.031
                        * 9*2048*S51/6265 = -3.28. 7*2038*S53/61651= 5.07. 738*2048*S55/61651= 0.747.
                        * The result is of the order 1.03, so we add 2 digits to S51 and S52 and one digit to S55.
                        */
                        int[] a51 = {31,-1614,-31,-6212,-31,-1614,31,74552} ; 
                        int[] a53 = {173,284,-173,-457,-173,284,173,-111} ; 
                        int[] a55 = {1,0,-1,-1,-1,0,1,1} ; 
                        BigDecimal S51 = broadhurstBBP(5,1,a51, new MathContext(2+mc.getPrecision()) ) ;
                        BigDecimal S53 = broadhurstBBP(5,3,a53, new MathContext(2+mc.getPrecision()) ) ;
                        BigDecimal S55 = broadhurstBBP(5,5,a55, new MathContext(1+mc.getPrecision()) ) ;
                        S51 = S51.multiply(new BigDecimal(18432)) ;
                        S53 = S53.multiply(new BigDecimal(14336)) ;
                        S55 = S55.multiply(new BigDecimal(1511424)) ;
                        return S51.add(S53).subtract(S55).divide(new BigDecimal(62651),mc) ;
                }
                else
                {
                        /* Cohen et al Exp Math 1 (1) (1992) 25
                        */
                        Rational betsum = new Rational() ;
                        Bernoulli bern = new Bernoulli() ;
                        Factorial fact = new Factorial() ;
                        for(int npr=0 ; npr <= (n+1)/2 ; npr++)
                        {
                                Rational b = bern.at(2*npr).multiply(bern.at(n+1-2*npr)) ;
                                b = b.divide(fact.at(2*npr)).divide(fact.at(n+1-2*npr)) ;
                                b = b.multiply(1-2*npr) ;
                                if ( npr % 2 ==0 )
                                        betsum = betsum.add(b) ;
                                else
                                        betsum = betsum.subtract(b) ;
                        }
                        betsum = betsum.divide(n-1) ;
                        /* The first term, including the facor (2pi)^n, is essentially most
                        * of the result, near one. The second term below is roughly in the range 0.003 to 0.009.
                        * So the precision here is matching the precisionn requested by mc, and the precision
                        * requested for 2*pi is in absolute terms adjusted.
                        */
                        MathContext mcloc = new MathContext( 2+mc.getPrecision() + (int)(Math.log10((double)(n))) ) ;
                        BigDecimal ftrm = pi(mcloc).multiply(new BigDecimal(2)) ;
                        ftrm = ftrm.pow(n) ;
                        ftrm = multiplyRound(ftrm, betsum.BigDecimalValue(mcloc) ) ;
                        BigDecimal exps = new BigDecimal(0) ;

                        /* the basic accuracy of the accumulated terms before multiplication with 2
                        */
                        double eps = Math.pow(10.,-mc.getPrecision()) ;

                        if ( n % 4 == 3)
                        {
                                /* since the argument n is at least 7 here, the drop
                                * of the terms is at rather constant pace at least 10^-3, for example
                                * 0.0018, 0.2e-7, 0.29e-11, 0.74e-15 etc for npr=1,2,3.... We want 2 times these terms
                                * fall below eps/10.
                                */
                                int kmax = mc.getPrecision()/3 ;
                                eps /= kmax ;
                                /* need an error of eps for 2/(exp(2pi)-1) = 0.0037
                                * The absolute error is 4*exp(2pi)*err(pi)/(exp(2pi)-1)^2=0.0075*err(pi)
                                */
                                BigDecimal exp2p = pi( new MathContext(3+err2prec(3.14, eps/0.0075)) ) ;
                                exp2p = exp(exp2p.multiply(new BigDecimal(2))) ;
                                BigDecimal c =  exp2p.subtract(BigDecimal.ONE) ;
                                exps = divideRound(1,c) ;
                                for(int npr=2 ; npr<= kmax ; npr++)
                                {
                                        /* the error estimate above for npr=1 is the worst case of
                                        * the absolute error created by an error in 2pi. So we can
                                        * safely re-use the exp2p value computed above without
                                        * reassessment of its error.
                                        */
                                        c =  powRound(exp2p,npr).subtract(BigDecimal.ONE) ;
                                        c =  multiplyRound(c, (new BigInteger(""+npr)).pow(n) ) ;
                                        c =  divideRound(1,c) ;
                                        exps = exps.add(c) ;
                                }
                        }
                        else
                        {
                                /* since the argument n is at least 9 here, the drop
                                * of the terms is at rather constant pace at least 10^-3, for example
                                * 0.0096, 0.5e-7, 0.3e-11, 0.6e-15 etc. We want these terms
                                * fall below eps/10.
                                */
                                int kmax = (1+mc.getPrecision())/3 ;
                                eps /= kmax ;
                                /* need an error of eps for 2/(exp(2pi)-1)*(1+4*Pi/8/(1-exp(-2pi)) = 0.0096
                                * at k=7 or = 0.00766 at k=13 for example.
                                * The absolute error is 0.017*err(pi) at k=9, 0.013*err(pi) at k=13, 0.012 at k=17
                                */
                                BigDecimal twop = pi( new MathContext(3+err2prec(3.14, eps/0.017)) ) ;
                                twop = twop.multiply(new BigDecimal(2)) ;
                                BigDecimal exp2p = exp(twop) ;
                                BigDecimal c =  exp2p.subtract(BigDecimal.ONE) ;
                                exps = divideRound(1,c) ;
                                c =  BigDecimal.ONE.subtract(divideRound(1,exp2p)) ;
                                c =  divideRound(twop,c).multiply(new BigDecimal(2)) ;
                                c =  divideRound(c,n-1).add(BigDecimal.ONE) ;
                                exps = multiplyRound(exps,c) ;
                                for(int npr=2 ; npr<= kmax ; npr++)
                                {
                                        c =  powRound(exp2p,npr).subtract(BigDecimal.ONE) ;
                                        c =  multiplyRound(c, (new BigInteger(""+npr)).pow(n) ) ;

                                        BigDecimal d =  divideRound(1, exp2p.pow(npr) ) ;
                                        d =  BigDecimal.ONE.subtract(d) ;
                                        d =  divideRound(twop,d).multiply(new BigDecimal(2*npr)) ;
                                        d =  divideRound(d,n-1).add(BigDecimal.ONE) ;

                                        d = divideRound(d,c) ;

                                        exps = exps.add(d) ;
                                }
                        }
                        exps = exps.multiply(new BigDecimal(2)) ;
                        return ftrm.subtract(exps,mc) ;
                }
        } /* zeta */

        /** Riemann zeta function.
        * @param n The positive integer argument.
        * @return zeta(n)-1.
        * @since 2009-08-20
        * @author Richard J. Mathar
        */
        static public double zeta1(final int n)
        {
                /* precomputed static table in double precision
                */
                final double[] zmin1 = {0.,0.,
6.449340668482264364724151666e-01,
2.020569031595942853997381615e-01,8.232323371113819151600369654e-02,
3.692775514336992633136548646e-02,1.734306198444913971451792979e-02,
8.349277381922826839797549850e-03,4.077356197944339378685238509e-03,
2.008392826082214417852769232e-03,9.945751278180853371459589003e-04,
4.941886041194645587022825265e-04,2.460865533080482986379980477e-04,
1.227133475784891467518365264e-04,6.124813505870482925854510514e-05,
3.058823630702049355172851064e-05,1.528225940865187173257148764e-05,
7.637197637899762273600293563e-06,3.817293264999839856461644622e-06,
1.908212716553938925656957795e-06,9.539620338727961131520386834e-07,
4.769329867878064631167196044e-07,2.384505027277329900036481868e-07,
1.192199259653110730677887189e-07,5.960818905125947961244020794e-08,
2.980350351465228018606370507e-08,1.490155482836504123465850663e-08,
7.450711789835429491981004171e-09,3.725334024788457054819204018e-09,
1.862659723513049006403909945e-09,9.313274324196681828717647350e-10,
4.656629065033784072989233251e-10,2.328311833676505492001455976e-10,
1.164155017270051977592973835e-10,5.820772087902700889243685989e-11,
2.910385044497099686929425228e-11,1.455192189104198423592963225e-11,
7.275959835057481014520869012e-12,3.637979547378651190237236356e-12,
1.818989650307065947584832101e-12,9.094947840263889282533118387e-13,
4.547473783042154026799112029e-13,2.273736845824652515226821578e-13,
1.136868407680227849349104838e-13,5.684341987627585609277182968e-14,
2.842170976889301855455073705e-14,1.421085482803160676983430714e-14,
7.105427395210852712877354480e-15,3.552713691337113673298469534e-15,
1.776356843579120327473349014e-15,8.881784210930815903096091386e-16,
4.440892103143813364197770940e-16,2.220446050798041983999320094e-16,
1.110223025141066133720544570e-16,5.551115124845481243723736590e-17,
2.775557562136124172581632454e-17,1.387778780972523276283909491e-17,
6.938893904544153697446085326e-18,3.469446952165922624744271496e-18,
1.734723476047576572048972970e-18,8.673617380119933728342055067e-19,
4.336808690020650487497023566e-19,2.168404344997219785013910168e-19,
1.084202172494241406301271117e-19,5.421010862456645410918700404e-20,
2.710505431223468831954621312e-20,1.355252715610116458148523400e-20,
6.776263578045189097995298742e-21,3.388131789020796818085703100e-21,
1.694065894509799165406492747e-21,8.470329472546998348246992609e-22,
4.235164736272833347862270483e-22,2.117582368136194731844209440e-22,
1.058791184068023385226500154e-22,5.293955920339870323813912303e-23,
2.646977960169852961134116684e-23,1.323488980084899080309451025e-23,
6.617444900424404067355245332e-24,3.308722450212171588946956384e-24,
1.654361225106075646229923677e-24,8.271806125530344403671105617e-25,
4.135903062765160926009382456e-25,2.067951531382576704395967919e-25,
1.033975765691287099328409559e-25,5.169878828456431320410133217e-26,
2.584939414228214268127761771e-26,1.292469707114106670038112612e-26,
6.462348535570531803438002161e-27,3.231174267785265386134814118e-27,
1.615587133892632521206011406e-27,8.077935669463162033158738186e-28,
4.038967834731580825622262813e-28,2.019483917365790349158762647e-28,
1.009741958682895153361925070e-28,5.048709793414475696084771173e-29,
2.524354896707237824467434194e-29,1.262177448353618904375399966e-29,
6.310887241768094495682609390e-30,3.155443620884047239109841220e-30,
1.577721810442023616644432780e-30,7.888609052210118073520537800e-31
                } ;
                if( n <= 0 )
                        throw new ProviderException("Not implemented: zeta at negative argument "+n) ;
                if( n == 1 )
                        throw new ArithmeticException("Pole at zeta(1) ") ;

                if( n < zmin1.length )
                        /* look it up if available */
                        return zmin1[n] ;
                else
                {
                        /* Result is roughly 2^(-n), desired accuracy 18 digits. If zeta(n) is computed, the equivalent accuracy
                        * in relative units is higher, because zeta is around 1.
                        */
                        double eps = 1.e-18*Math.pow(2.,(double)(-n) ) ;
                        MathContext mc = new MathContext( err2prec(eps) ) ;
                        return zeta(n,mc).subtract(BigDecimal.ONE).doubleValue() ;
                }
        } /* zeta */

        /** Digamma function.
        * @param x The main argument.
        * @return psi(x).
        *  The error is sometimes up to 10 ulp, where AS 6.3.15 suffers from cancellation of digits and psi=0
        * @since 2009-08-26
        * @author Richard J. Mathar
        */
        static public double psi(final double x)
        {
                /* the single positive zero of psi(x)
                */
                final double psi0 = 1.46163214496836234126265954232572132846819;
                if ( x > 2.0)
                {
                        /* Reduce to a value near x=1 with the standard recurrence formula.
                        * Abramowitz-Stegun 6.3.5
                        */
                        int m = (int) ( x-0.5 );
                        double xmin1 = x-m ;
                        double resul = 0. ;
                        for(int i=1; i <= m ; i++)
                                resul += 1./(x-i) ;
                        return resul+psi(xmin1) ;
                }
                else if ( Math.abs(x-psi0) < 0.55)
                {
                        /* Taylor approximation around the local zero
                        */
                        final double [] psiT0 = { 9.67672245447621170427e-01, -4.42763168983592106093e-01,
                        2.58499760955651010624e-01, -1.63942705442406527504e-01, 1.07824050691262365757e-01,
                        -7.21995612564547109261e-02, 4.88042881641431072251e-02, -3.31611264748473592923e-02,
                        2.25976482322181046596e-02, -1.54247659049489591388e-02, 1.05387916166121753881e-02,
                        -7.20453438635686824097e-03, 4.92678139572985344635e-03, -3.36980165543932808279e-03,
                        2.30512632673492783694e-03, -1.57693677143019725927e-03, 1.07882520191629658069e-03,
                        -7.38070938996005129566e-04, 5.04953265834602035177e-04, -3.45468025106307699556e-04,
                        2.36356015640270527924e-04, -1.61706220919748034494e-04, 1.10633727687474109041e-04,
                        -7.56917958219506591924e-05, 5.17857579522208086899e-05, -3.54300709476596063157e-05,
                        2.42400661186013176527e-05, -1.65842422718541333752e-05, 1.13463845846638498067e-05,
                        -7.76281766846209442527e-06, 5.31106092088986338732e-06, -3.63365078980104566837e-06,
                        2.48602273312953794890e-06, -1.70085388543326065825e-06, 1.16366753635488427029e-06,
                        -7.96142543124197040035e-07, 5.44694193066944527850e-07, -3.72661612834382295890e-07,
                        2.54962655202155425666e-07, -1.74436951177277452181e-07, 1.19343948298302427790e-07,
                        -8.16511518948840884084e-08, 5.58629968353217144428e-08, -3.82196006191749421243e-08,
                        2.61485769519618662795e-08, -1.78899848649114926515e-08, 1.22397314032336619391e-08,
                        -8.37401629767179054290e-09, 5.72922285984999377160e-09} ;
                        final double xdiff = x-psi0 ;
                        double resul = 0. ;
                        for( int i = psiT0.length-1; i >=0 ; i--)
                                resul = resul*xdiff+psiT0[i] ;
                        return resul*xdiff ;
                }
                else if ( x < 0. )
                {
                        /* Reflection formula */
                        double xmin = 1.-x ;
                        return psi(xmin) + Math.PI/Math.tan(Math.PI*xmin) ;
                }
                else
                {
                        double xmin1 = x-1 ;
                        double resul = 0. ;
                        for(int k=26 ; k>= 1; k--)
                        {
                                resul -= zeta1(2*k+1) ;
                                resul *= xmin1*xmin1 ;
                        }
                                /* 0.422... = 1 -gamma */
                        return resul + 0.422784335098467139393487909917597568 
                                + 0.5/xmin1-1./(1-xmin1*xmin1)- Math.PI/( 2.*Math.tan(Math.PI*xmin1) );
                }
        } /* psi */


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
        * @param f The right factor.
        * @return The product x*f.
        * @since 2009-07-30
        * @author Richard J. Mathar
        */
        static public BigDecimal multiplyRound(final BigDecimal x, final Rational f)
        {
                if (  f.compareTo(BigInteger.ZERO) == 0 ) 
                        return BigDecimal.ZERO ;
                else
                {
                        /* Convert the rational value with two digits of extra precision
                        */
                        MathContext mc = new MathContext( 2+x.precision() ) ;
                        BigDecimal fbd = f.BigDecimalValue(mc) ;

                        /* and the precision of the product is then dominated by the precision in x
                        */
                        return multiplyRound(x,fbd) ;
                }
        }

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

        /** Multiply and round.
        * @param x The left factor.
        * @param n The right factor.
        * @return the product x*n
        * @since 2009-07-30
        * @author Richard J. Mathar
        */
        static public BigDecimal multiplyRound(final BigDecimal x, final BigInteger n)
        {
                BigDecimal resul = x.multiply(new BigDecimal(n)) ;
                /* The estimation of the absolute error in the result is |n*err(x)|
                */
                MathContext mc = new MathContext( n.compareTo(BigInteger.ZERO) != 0 ? x.precision(): 0 ) ;
                return resul.round(mc) ;
        }

        /** Divide and round.
        * @param x The numerator
        * @param y The denominator
        * @return the divided x/y
        * @since 2009-07-30
        * @author Richard J. Mathar
        */
        static public BigDecimal divideRound(final BigDecimal x, final BigDecimal y)
        {
                /* The estimation of the relative error in the result is |err(y)/y|+|err(x)/x| 
                */
                MathContext mc = new MathContext( Math.min(x.precision(),y.precision()) ) ;
                BigDecimal resul = x.divide(y,mc) ;
                /* If x and y are precise integer values that may have common factors,
                * the method above will truncate trailing zeros, which may result in
                * a smaller apparent accuracy than starte... add missing trailing zeros now.
                */
                return scalePrec(resul,mc) ;
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

        /** Divide and round.
        * @param n The numerator.
        * @param x The denominator.
        * @return the divided n/x.
        * @since 2009-08-05
        * @author Richard J. Mathar
        */
        static public BigDecimal divideRound(final int n, final BigDecimal x)
        {
                /* The estimation of the relative error in the result is |err(x)/x| 
                */
                MathContext mc = new MathContext( x.precision() ) ;
                return new BigDecimal(n).divide(x,mc) ;
        }

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
