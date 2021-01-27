# `csw-json-xml-converter`
Convert's JSON data into XML data with the following constrains.
JSON supports the following types. 

The following types of JSON values are supported here :
 1. Number
 2. String
 3. Boolean
 4. Array
 5. Object
 6. null

Only JSON objects and arrays are supported as top level values.
Refer Few of the conversion samples provided below. 

#### `1.1 Single Number`
##### <font color="brown">JSON :</font>

```json
-5
```

##### <font color="green">XML  :</font>

```xml
<number>-5</number>
```

#### `1.2 Nested Numbers`
##### <font color="brown">JSON :</font>

```json
{
  "cars": 2,
  "trucks": 10
}
```

##### <font color="green">XML  :</font>

```xml
<object>
  <number  name="cars">2</number>
  <number  name="trucks">10</number>
</object>
```

#### `2.1 Single String`
##### <font color="brown">JSON :</font>

```json
"Hello World"
```

##### <font color="green">XML  :</font>

```xml
<string>Hello World</string>
```

#### `2.2 Nested String`
##### <font color="brown">JSON :</font>

```json
{
  "firstName": "John",
  "lastName": "Smith"
}
```

##### <font color="green">XML  :</font>

```xml
<object>
  <string name="firstName">John</string>
  <string name="lastName">Smith</string>
</object>
```

#### `3.1 Single Boolean`
##### <font color="brown">JSON :</font>

```json
true
```

##### <font color="green">XML  :</font>

```xml
<boolean>true</boolean>
```

#### `3.2 Nested Boolean`
##### <font color="brown">JSON :</font>

```json
{
  "isHuman": true,
  "isTall": false
}
```

##### <font color="green">XML  :</font>

```xml
<object>
  <boolean name="isHuman">true</boolean>
  <boolean name="isTall">false</boolean>
</object>
```

#### `4.1 Single Array`
##### <font color="brown">JSON :</font>

```json
[1, "test"]
```

##### <font color="green">XML  :</font>

```xml
<array>
  <number>1</number>
  <string>test</string>
</array>
```

#### `4.2 Nested Array`
##### <font color="brown">JSON :</font>

```json
{
  "fibs": [0,1,1,2,3,"fibs",true]
}
```

##### <font color="green">XML  :</font>

```xml
<object>
  <array>
    <number>0</number>
    <number>1</number>
    <number>1</number>
    <number>2</number>
    <number>3</number>
    <string>fibs</string>
    <boolean>true</boolean>
  </array>
</object>
```

#### `5. Object`
##### <font color="brown">JSON :</font>

```json
{
  "profile": {
    "firstName": "John",
    "lastName": "Smith",
    "age": 20,
    "friends": [
  	  "Joe",
  	  {
  	    "firstName": "Sue",
  	    "lastName": "Jones"
  	  }
    ]
  }
}
```

##### <font color="green">XML  :</font>

```xml
<object>
  <object name="profile">
    <string name="firstName">John</string>
    <string name="lastName">Smith</string>
    <number name="age">20</number>
    <array name="friends">
      <string>Joe</string>
      <object>
        <string name="firstName">Sue</string>
        <string name="lastName">Jones</string>
      </object>
    </array>
  </object>
</object>
```

#### `6.1 Single null`
##### <font color="brown">JSON :</font>

```json
null
```

##### <font color="green">XML  :</font>

```xml
<null/>
```

#### `6.2 Nested null`
##### <font color="brown">JSON :</font>

```json
{
  "computer_name": null
}
```

##### <font color="green">XML  :</font>

```xml
<object>
  <null name="computer_name">true />
</object>
```

## `How to Run` 

###### `[Note : Supports only on linux machines]`

#### `Step 1 :`

Download [csw-json-xml-converter.sh](csw-json-xml-converter.sh) from this Repo and place it anywhere in your linux machine.

#### `Step 2 :`

Change the file to executable. To make it executable, use below command

	chmod +x csw-json-xml-converter.sh

#### `Step 3 :`

The `csw-json-xml-converter.sh` shell script executes in 3 modes.

 1. Install
 2. Update
 3. Run

The 1st parameter for this shell file defines the execution mode and is case in-sensitive.
The 1st parameter supports only above 3 values.

##### `1. Install Mode`

In Install mode :
 - Accepts only 1 argument.
 - The necessary tools like Git, Java, Gradle, etc., were Installed.
 - The Repo is cloned from Github.
 - The Jar file for this Repo will be created using Gradle. 

To execute the script in `Install` mode, use below command.

	./csw-json-xml-converter.sh install

When Install mode is triggered again, the existing setup folder will be moved to a backup folder and a new setup will be done.

##### `2. Update Mode`

Once Install mode succeeds, we can trigger the Update mode.
In Update mode :
 - Accepts only 1 argument.
 - The Repo will be updated by using `git` commands.
 - The Jar file for this updated Repo will be created using Gradle.

To execute the script in `Update` mode, use below command.

	./csw-json-xml-converter.sh update

##### `3. Run Mode`

Once Install mode succeeds, we can trigger the Run mode.
In Run mode :
 - Accepts 3 arguments.
 - The Last 2 Parameters were input and output path respectively
 - The Jar file will be executed using java commands.

To execute the script in `Run` mode, use below command.

	./csw-json-xml-converter.sh run /opt/json-files/sample-input.json /opt/xml-files/sample-output.json

The 2nd parameter is the input file path, must be a valid JSON file. The 3rd parameter is the out file path, must be a valid XML file.

The XML file may not be available at the specified location, it will be created by the application. If present already, it will be overwritten by the application.

The 3rd parameter me be a directory, where the new XML output file will be created in the name of the JSON file. 

	./csw-json-xml-converter.sh run /opt/json-files/object-sample.json /opt/xml-files/

For the above command, the output will be generated into `/opt/xml-files/object-sample.json`.

##### Note : Provide absolute file paths only, error will be thrown otherwise, considering as invalid file path.
