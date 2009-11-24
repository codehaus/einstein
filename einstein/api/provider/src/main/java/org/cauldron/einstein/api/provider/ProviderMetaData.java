package org.cauldron.einstein.api.provider;

import org.cauldron.einstein.api.common.metadata.CoreMetaData;
import org.cauldron.einstein.api.model.resource.Facade;

import java.lang.annotation.Documented;

/**
 * Provides comprehensive meta data about the provider, which can be used by the compiler/runtime when an error occurs
 * or by the compiler to provide a help page for the provider.
 *
 * @author Neil Ellis
 */
@Documented
public @interface ProviderMetaData {

    CoreMetaData core();

    /**
     * If true, the Facade is capable of supporting all Facades, and is not URI dependent.
     *
     * @return true if all Facades are supported.
     */
    boolean supportsAll() default false;

    /**
     * These Facades are always supported, this value is ignored if supportsAll() is true.
     *
     * @return a list of always supported facades, or null if none or are optional or if supportsAll() is true.
     */
    Class<? extends Facade>[] alwaysSupported();

    /**
     * If true, the Facade is capable theoretically of supporting all Facades, but is URI dependent. This is true of
     * delegating providers like java: where the resource pointed to actually implements the Facades.
     *
     * @return true if potentially all Facades are supported, but is dependent on the URI.
     */
    boolean optionallySupportsAll() default false;

    /**
     * These Facades may be supported, depending on the URI specified. The value of this is ignored if
     * optionallySupportsAll() is true.
     *
     * @return a list of optionally supported facades, or empty if none or are optional or if optionallySupportsAll() is
     *         true.
     */
    Class<? extends Facade>[] optionallySupported() default {};

}
