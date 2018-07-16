'use strict';
const Generator = require('yeoman-generator');

const request = require('request-promise-native');
const cheerio = require('cheerio');

module.exports = class extends Generator {
    constructor(args, opts) {
        super(args, opts);

        this.javaPath = "src/main/java/";
        this.resourcePath = "src/main/resources/";
        this.basePackage = "org/dave/todo/";
        this.classPath = this.javaPath + this.basePackage;
        this.assetPath = this.resourcePath + "assets/todo/";
    }

    initializing() {
        return Promise.all([
            this._fetchForgeVersion(),
            this._fetchLatestMavenVersion('http://dvs1.progwml6.com/files/maven/mezz/jei/jei_1.12.2/', 'jeiVersion'),
            this._fetchLatestMavenVersion('https://maven.tterrag.com/index.php?dir=mcjty/theoneprobe/TheOneProbe-1.12', 'topVersion'),
        ]);
    }

    _fetchForgeVersion() {
        return new Promise((resolve, reject) => {
            this.log("Fetching latest Forge version");
            request('https://files.minecraftforge.net/').then(html => {
                let $ = cheerio.load(html);

                let lfV = undefined;
                $('a.info-link').each(function(num, element) {
                    let url = $(this).attr('href');
                    if(url == undefined) {
                        return true;
                    }
                    let res = url.match(/forge-1\.12\.2-(.*?)-mdk\.zip$/)
                    if(res == null || res == undefined) {
                        return true;
                    }

                    lfV = res[1];
                    return false;
                });

                this.latestForgeVersion = lfV;
                resolve(lfV);
            }).catch(reject);
        });
    }

    _fetchLatestMavenVersion(mavenUrl, propName) {
        return new Promise((resolve, reject) => {
            this.log(`Fetching latest Maven artifact version from: ${mavenUrl}`);
            request(mavenUrl + '/maven-metadata.xml').then(html => {
                let $ = cheerio.load(html);
                let release = $('release').first();

                let version = release.text();
                this[propName] = version;
                resolve(version);
            }).catch(reject);
        });
    }

    prompting() {
        var prompts = [{
            name    : 'modname',
            message : 'Mod name',
            default : this.modname ? this.modname : this.appname
        }, {
            name    : 'modid',
            message : 'Mod id',
            default : function(props) {
                return props.modname.replace(/\s/g, '').toLowerCase();
            },
            validate: function(input) {
                return !/\s/g.test(input) && /^[a-z]+$/.test(input);
            }
        }, {
            name    : 'description',
            message : 'Mod description'
        }, {
            name    : 'author',
            message : 'Your developer name',
            default : 'Davenonymous',
            store   : true
        }, {
            name    : 'credits',
            message : 'Credits',
            default : function(props) {
                return props.author;
            }
        }, {
            name    : 'group',
            message : 'The Java package name',
            default : function(props) {
                return "org.dave." + props.modid;
            },
            validate: function(input) {
                return !/\s/g.test(input) && /^[a-z\.]+$/.test(input);
            }
        }, {
            name    : 'baseClass',
            message : 'The mod\'s classname',
            default : function(props) {
                return props.modname.replace(/\s/g, '');
            },
            validate: function(input) {
                return !/\s/g.test(input) && !/\./.test(input);
            }
        }, {
            name    : 'archivesBaseName',
            message : 'The Jar base name',
            default : function(props) {
                return props.modname.replace(/\s/g, '');
            },
            validate: function(input) {
                return !/\s/g.test(input);
            }
        }, {
            name    : 'githubUser',
            message : 'Github user name',
            default : function(props) {
                return "thraaawn"
            },
            validate: function(input) {
                return !/\s/g.test(input);
            }
        },
        {
            name    : 'repo',
            message : 'Github repo location',
            default : function(props) {
                return props.githubUser + "/" + props.modname.replace(/\s/g, '');
            },
            validate: function(input) {
                return !/\s/g.test(input);
            }
        },
        {
            name    : 'forgeVersion',
            message : 'Forge Version to use',
            default : this.latestForgeVersion,
            validate: function(input) {
                return /^\d+\.\d+\.\d+\.\d+$/g.test(input);
            }
        },
        {
            type: 'checkbox',
            name: 'dependencies',
            message: 'What dependencies do you want to include',
            choices: [
                { name: "Just Enough Items", checked: true },
                { name: "The One Probe", checked: true },
            ]
        }];

        return this.prompt(prompts).then(props => {
            this.props = props;
        });
    }

    copy() {
        let props = this.props;
        props.jeiVersion = this.jeiVersion;
        props.topVersion = this.topVersion;

        console.log(props);

        var tplFiles = [
            'build.gradle',
            'build.properties',
            `${this.resourcePath}mcmod.info`,
            `${this.resourcePath}pack.mcmeta`,
        ];

        var cpFiles = [
            'gradle.properties',
            'gradlew',
            'gradlew.bat',
            '.travis.yml',
            '.gitignore',
            'gradle/wrapper/gradle-wrapper.jar',
            'gradle/wrapper/gradle-wrapper.properties'
        ];

        var assetFiles = [
            'lang/en_us.lang',
            'models/item/.gitkeep',
            'models/block/.gitkeep',
            'textures/blocks/.gitkeep',
            'textures/items/.gitkeep',
            'textures/gui/.gitkeep',
            'blockstates/.gitkeep',
        ];

        var javaFiles = [
            'util/Logz.java',
            'util/ResourceLoader.java',
            'proxy/CommonProxy.java',
            'proxy/ClientProxy.java',
            'proxy/ServerProxy.java',
            'init/Blockss.java',
            'init/Itemss.java',
            'config/ConfigurationHandler.java',
        ];

        tplFiles.forEach(file => {
            this.fs.copyTpl(this.templatePath(file), this.destinationPath(file), props);
        });

        cpFiles.forEach(file => {
            this.fs.copy(this.templatePath(file), this.destinationPath(file));
        });

        assetFiles.forEach(file => {
            let srcPath = this.templatePath(`${this.resourcePath}assets/${file}`);
            let destPath = this.destinationPath(`${this.resourcePath}assets/${props.modid}/${file}`);
            this.fs.copyTpl(srcPath, destPath, props);
        });

        assetFiles.forEach(file => {
            let srcPath = this.templatePath(`${this.resourcePath}assets/${file}`);
            let destPath = this.destinationPath(`${this.resourcePath}assets/${props.modid}/${file}`);
            this.fs.copyTpl(srcPath, destPath, props);
        });

        let packagePath = props.group.replace(/\./g, '/');
        javaFiles.forEach(file => {
            let srcPath = this.templatePath(`${this.javaPath}/${file}`);
            let destPath = this.destinationPath(`${this.javaPath}/${packagePath}/${file}`);
            this.fs.copyTpl(srcPath, destPath, props);
        });

        this.fs.copyTpl(this.templatePath(`${this.javaPath}/Main.java`), this.destinationPath(`${this.javaPath}/${packagePath}/${props.baseClass}.java`), props);
    }
};
