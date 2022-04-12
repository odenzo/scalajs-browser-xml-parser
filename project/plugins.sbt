libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
libraryDependencies += "org.scala-js" %% "scalajs-env-selenium"     % "1.1.1"

//addSbtPlugin("com.codecommit"     % "sbt-github-packages"      % "0.5.3")
addSbtPlugin("com.github.sbt"     % "sbt-native-packager"      % "1.9.7")  //  https://github.com/sbt/sbt-native-packager
addSbtPlugin("org.scala-js"       % "sbt-scalajs"              % "1.9.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
addSbtPlugin("io.spray"           % "sbt-revolver"             % "0.9.1")  // https://github.com/spray/sbt-revolver
addSbtPlugin("com.eed3si9n"       % "sbt-assembly"             % "1.1.0")  // https://github.com/sbt/sbt-assembly
addSbtPlugin("de.gccc.sbt"        % "sbt-jib"                  % "0.10.0") // https://github.com/schmitch/sbt-jib

// For Node.js with JSDOM testing, wish there was a recent Phoenix or somewting.
//https://www.scala-js.org/doc/project/js-environments.html
// https://mvnrepository.com/artifact/org.scala-js/scalajs-junit-test-plugin
//libraryDependencies += "org.scala-js" %% "scalajs-junit-test-plugin" % "1.10.0"
//addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.20.0") // NPM Bundling
//addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.20.0") // to integrate with sbt-web instead of scalaxjs bunlder above

//addSbtPlugin("org.scalablytyped.converter" % "sbt-converter" % "1.0.0-beta37")
