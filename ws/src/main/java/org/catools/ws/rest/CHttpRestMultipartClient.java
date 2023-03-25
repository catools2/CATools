package org.catools.ws.rest;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import org.catools.common.io.CFile;
import org.catools.common.io.CResource;
import org.catools.common.tests.CTest;
import org.catools.ws.enums.CHttpRequestType;

import java.io.File;
import java.io.InputStream;

public abstract class CHttpRestMultipartClient<T extends CTest, O> extends CHttpRestClient<T, O> {

  public CHttpRestMultipartClient(T testInstance, CHttpRequestType requestType, String targetURL) {
    this(testInstance, requestType, targetURL, null);
  }

  public CHttpRestMultipartClient(
      T testInstance, CHttpRequestType requestType, String targetURL, String targetPath) {
    super(testInstance, requestType, targetURL, targetPath);
  }

  public CHttpRestMultipartClient<T, O> addFile(
      String controlName, File file, ContentType contentType) {
    addFile(controlName, file.getName(), new CFile(file).getBytes(), contentType);
    return this;
  }

  public CHttpRestMultipartClient<T, O> addFile(
      String controlName, String fileName, File file, ContentType contentType) {
    addFile(controlName, fileName, new CFile(file).getBytes(), contentType);
    return this;
  }

  public CHttpRestMultipartClient<T, O> addFile(
      String controlName, CResource resource, ContentType contentType) {
    addFile(controlName, resource.getResourceName(), resource.getByteArray(), contentType);
    return this;
  }

  public CHttpRestMultipartClient<T, O> addFile(
      String controlName, String fileName, CResource resource, ContentType contentType) {
    addFile(controlName, fileName, resource.getByteArray(), contentType);
    return this;
  }

  public CHttpRestMultipartClient<T, O> addFile(
      String controlName, String fileName, InputStream stream, ContentType contentType) {
    addPart(
        new MultiPartSpecBuilder(stream)
            .controlName(controlName)
            .fileName(fileName)
            .mimeType(contentType.toString())
            .build());
    return this;
  }

  public CHttpRestMultipartClient<T, O> addFile(String controlName, String fileName, byte[] bytes) {
    addPart(new MultiPartSpecBuilder(bytes).controlName(controlName).fileName(fileName).build());
    return this;
  }

  public CHttpRestMultipartClient<T, O> addFile(
      String controlName, String fileName, byte[] bytes, ContentType contentType) {
    addPart(
        new MultiPartSpecBuilder(bytes)
            .controlName(controlName)
            .fileName(fileName)
            .mimeType(contentType.toString())
            .build());
    return this;
  }

  public CHttpRestMultipartClient<T, O> addPart(
      String controlName, String content, ContentType contentType) {
    addPart(
        new MultiPartSpecBuilder(content)
            .controlName(controlName)
            .mimeType(contentType.toString())
            .build());
    return this;
  }

  public CHttpRestMultipartClient<T, O> addPart(MultiPartSpecification multiPartSpecification) {
    this.getRequest().getMultiParts().add(multiPartSpecification);
    return this;
  }
}
