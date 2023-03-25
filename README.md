# CATools

I like coding and finding different solutions for the same problem.  
It caused me to have lots of code that needs to be clean up.
So every time I changed a project, I just deleted everything and start over.

Due to the size of the project and intensive SDLC, I rarely used any of them on the real project and they always stay as
a sandbox until they've been deleted.

This CA-Tools was my 6th one focused on unstable infrastructure and SUT with aggressive delivery driven SDLC.
I had a sudden request in one of my projects and somehow, these tools happen to be just what I needed.
So after cleaning the house, I decided to use them therefore I put them in GitLab so anyone can contribute if needed and
deploy them in central with source codes just in case.

The majority of the challenges I faced are very specific to the project, so I am not sure if you need to use the whole
code so feel free to use libs if that works for you or copy/paste any part of the code you want.

If you have any questions or recommendation I will be more than glad to know about them. I love new challenges and in
this journey, there is no judgment, bad or wrong knowledge so do not hesitate to share your thoughts by skype (
akeshmiri) or email (kimiak2000@gmail.com).

- Tools:

    - Java 11 (https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_windows-x64_bin.zip)
    - Maven 3.6.3 or later

- Add Lombok and Enable Annotation Processing for your idea
- put following line in your testNG Run/Debug configuration for VM parameter (we need this for JMockit)

```
    -ea -javaagent:/root/.m2/repository/org/jmockit/jmockit/1.44/jmockit-1.44.jar
```

#GPG
Follow [gpg_signed_commits](https://docs.gitlab.com/ee/user/project/repository/gpg_signed_commits/)

##Send Key To Server

```
gpg2 --send-keys --keyserver hkps://keyserver.ubuntu.com XXXXXX
```

##Receive Key To Server

```
gpg2 --keyserver hkps://keyserver.ubuntu.com --receive-key XXXXXX
```

##Export Keys

```
gpg --output pubkey.gpg --armor --export XXXXXX
gpg --output seckey.gpg --armor --export-secret-key XXXXXX
```

##Import Keys

```
gpg --import pub
gpg --allow-secret-key-import --import key

```
