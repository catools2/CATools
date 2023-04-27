package org.catools.common.extensions.verify.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.catools.common.extensions.verify.CVerificationInfo;
import org.catools.common.extensions.verify.CVerificationQueue;
import org.catools.common.extensions.wait.interfaces.CBaseWaiter;
import org.catools.common.utils.CStringUtil;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * CBaseVerifier is an interface to hold shared method between all verifier classes.
 */
//
public interface CBaseVerifier<O> extends CBaseWaiter<O>, CVerificationQueue {

  @SuppressWarnings("unchecked")
  default <A, B> void _verify(
      Function<O, A> actualProvider,
      Supplier<B> expectedSupplier,
      boolean printDiff,
      BiFunction<A, B, Boolean> verifyMethod,
      final String message,
      final Object... params) {
    if (_useWaiter()) {
      queue(
          new CVerificationInfo(
              () -> actualProvider.apply(get()),
              (Supplier<Object>) expectedSupplier,
              CStringUtil.format(message, params),
              printDiff,
              getDefaultWaitInSeconds(),
              getDefaultWaitIntervalInMilliSeconds(),
              (BiFunction<Object, Object, Boolean>) verifyMethod));
    } else {
      queue(
          new CVerificationInfo(
              () -> actualProvider.apply(get()),
              (Supplier<Object>) expectedSupplier,
              CStringUtil.format(message, params),
              printDiff,
              (BiFunction<Object, Object, Boolean>) verifyMethod));
    }
  }

  @SuppressWarnings("unchecked")
  default <B> void _verify(
      O actual,
      B expected,
      boolean printDiff,
      BiFunction<O, B, Boolean> verifyMethod,
      final String message,
      final Object... params) {
    if (_useWaiter()) {
      queue(
          new CVerificationInfo(
              () -> actual,
              () -> expected,
              CStringUtil.format(message, params),
              printDiff,
              getDefaultWaitInSeconds(),
              getDefaultWaitIntervalInMilliSeconds(),
              (BiFunction<Object, Object, Boolean>) verifyMethod));
    } else {
      queue(
          new CVerificationInfo(
              () -> actual,
              () -> expected,
              CStringUtil.format(message, params),
              printDiff,
              (BiFunction<Object, Object, Boolean>) verifyMethod));
    }
  }

  @SuppressWarnings("unchecked")
  default <B> void _verify(
      O actual,
      B expected,
      boolean printDiff,
      BiFunction<O, B, Boolean> verifyMethod,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    queue(
        new CVerificationInfo(
            () -> actual,
            () -> expected,
            CStringUtil.format(message, params),
            printDiff,
            waitInSeconds,
            intervalInMilliSeconds,
            (BiFunction<Object, Object, Boolean>) verifyMethod));
  }

  @SuppressWarnings("unchecked")
  default <B> void _verify(
      B expected,
      boolean printDiff,
      BiFunction<O, B, Boolean> verifyMethod,
      final String message,
      final Object... params) {
    if (_useWaiter()) {
      queue(
          new CVerificationInfo(
              this::get,
              () -> expected,
              CStringUtil.format(message, params),
              printDiff,
              getDefaultWaitInSeconds(),
              getDefaultWaitIntervalInMilliSeconds(),
              (BiFunction<Object, Object, Boolean>) verifyMethod));
    } else {
      queue(
          new CVerificationInfo(
              this::get,
              () -> expected,
              CStringUtil.format(message, params),
              printDiff,
              (BiFunction<Object, Object, Boolean>) verifyMethod));
    }
  }

  @SuppressWarnings("unchecked")
  default <B> void _verify(
      B expected,
      boolean printDiff,
      BiFunction<O, B, Boolean> verifyMethod,
      BiConsumer<O, B> onFail,
      final String message,
      final Object... params) {
    if (_useWaiter()) {
      queue(
          new CVerificationInfo(
              this::get,
              () -> expected,
              CStringUtil.format(message, params),
              printDiff,
              getDefaultWaitInSeconds(),
              getDefaultWaitIntervalInMilliSeconds(),
              (BiFunction<Object, Object, Boolean>) verifyMethod,
              (BiConsumer<Object, Object>) onFail));
    } else {
      queue(
          new CVerificationInfo(
              this::get,
              () -> expected,
              CStringUtil.format(message, params),
              printDiff,
              (BiFunction<Object, Object, Boolean>) verifyMethod,
              (BiConsumer<Object, Object>) onFail));
    }
  }

  default <B> void _verify(
      B expected,
      boolean printDiff,
      BiFunction<O, B, Boolean> verifyMethod,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    _verify(
        expected,
        printDiff,
        verifyMethod,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  default <B> void _verify(
      B expected,
      boolean printDiff,
      BiFunction<O, B, Boolean> verifyMethod,
      BiConsumer<O, B> onFail,
      final int waitInSeconds,
      final String message,
      final Object... params) {
    _verify(
        expected,
        printDiff,
        verifyMethod,
        onFail,
        waitInSeconds,
        getDefaultWaitIntervalInMilliSeconds(),
        message,
        params);
  }

  @SuppressWarnings("unchecked")
  default <B> void _verify(
      B expected,
      boolean printDiff,
      BiFunction<O, B, Boolean> verifyMethod,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    queue(
        new CVerificationInfo(
            this::get,
            () -> expected,
            CStringUtil.format(message, params),
            printDiff,
            waitInSeconds,
            intervalInMilliSeconds,
            (BiFunction<Object, Object, Boolean>) verifyMethod));
  }

  @SuppressWarnings("unchecked")
  default <A, B> void _verify(
      B expected,
      boolean printDiff,
      BiFunction<A, B, Boolean> verifyMethod,
      BiConsumer<A, B> onFail,
      final int waitInSeconds,
      final int intervalInMilliSeconds,
      final String message,
      final Object... params) {
    queue(
        new CVerificationInfo(
            () -> (A) get(),
            () -> expected,
            CStringUtil.format(message, params),
            printDiff,
            waitInSeconds,
            intervalInMilliSeconds,
            (BiFunction<Object, Object, Boolean>) verifyMethod,
            (BiConsumer<Object, Object>) onFail));
  }

  default String getDefaultMessage(final String methodDescription, final Object... params) {
    return getDefaultMessage(String.format(methodDescription, params));
  }

  default String getDefaultMessage(final String methodDescription) {
    if (CStringUtil.isBlank(getVerifyMessagePrefix())) {
      return "Verify " + methodDescription + ".";
    }
    return String.format("Verify %s %s.", getVerifyMessagePrefix(), methodDescription);
  }

  boolean _useWaiter();

  @JsonIgnore
  default String getVerifyMessagePrefix() {
    return "";
  }
}
