package stacklayout.helper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to show {@link stacklayout.helper.DefaultWrappingInflater} that
 * a view that just has been inflated
 * should be of StackLayout's full size despite of any padding that has been set for StackLayout.
 * This annotation could be used for showing fullscreen popups.
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoPaddingContainer {
}
