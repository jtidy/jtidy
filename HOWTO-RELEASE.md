# How to release to Maven Central

To prepare a release, you must have published a PGP signing key to a public key server like "keys.openpgp.org".

You need an account at https://issues.sonatype.org/ with user-name and password to upload to a staging repository for 
releasing to maven-central. This account must be linked to the JTidy project. Adding a new user can be done by filing 
a Jira ticket there. 

The Sonatype account must be set up in your Maven settings file `~/.m2/settings.xml`:

```
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>your-user-name</username>
      <password>your-password</password>
    </server>
  </servers>
</settings>
```

## Set stable version number

First, a new version number must be assigned to the project and everything must be committed to the repository. Its 
best to switch to a new branch from `master` before. If anything goes wrong, this makes it easy to roll back the 
changes without affecting the master branch.  

```
mvn release:clean release:prepare
```

## Create staging repository

If the previous command completed without errors, the new release can be built and uploaded to maven-central.

```
mvn release:perform
```

If this also worked fine, you can update the project page to announce the new version and merge your release branch 
back to the master branch.
