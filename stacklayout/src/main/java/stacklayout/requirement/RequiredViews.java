package stacklayout.requirement;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to show to {@link stacklayout.requirement.DefaultRequirementsAnalyzer} that
 * this view requires some other views to be in the StackLayout to function properly.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredViews {
    Class[] value() default {};
}
