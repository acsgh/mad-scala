import path from 'path'

import MiniCssExtractPlugin from 'mini-css-extract-plugin';
import CleanWebpackPlugin from 'webpack-clean'
import CopyWebpackPlugin from 'copy-webpack-plugin'
import ScriptExtHtmlWebpackPlugin from 'script-ext-html-webpack-plugin';
const HtmlWebpackPlugin = require('html-webpack-plugin');


module.exports = env => {

    return {
        mode: 'production',
        entry: (() => {
            return {
                'app': './src/index.js',
                'styles': './src/assets/styles/styles.scss',
            };
        })(),
        output: {
            path: path.resolve(__dirname, `${env.OUTPUT_FOLDER}`),
            filename: '[name].bundle.js'
        },
        module: {
            rules: [
                {
                    test: /\.jsx?$/,
                    exclude: /node_modules/,
                    use: 'babel-loader'
                },
                {
                    test: /\.(svg|png|jpe?g|gif)$/,
                    exclude: /node_modules/,
                    use: 'file-loader?name=img/[name].[ext]'
                },
                {
                    test: /\.((woff2?|svg)(\?(\w|-|#)+))|(woff2?|svg|jpe?g|png|gif|ico)$/,
                    use: 'url-loader?limit=10000&name=i/[name].[ext]'
                },
                {
                    test: /\.((ttf|eot)(\?(\w|-|#)+))|(ttf|eot)$/,
                    use: 'file-loader?name=i/[name].[ext]'
                },
                {
                    test: /\.scss$/,
                    exclude: /node_modules/,
                    use: [
                        {loader: MiniCssExtractPlugin.loader},
                        // { loader: 'style-loader' },
                        {loader: 'css-loader'},
                        {loader: 'sass-loader'}
                    ]
                }
            ]
        },
        devServer: {
            compress: true,
            port: 3000,
        },
        plugins: (() => {
            let plugins = [
                new MiniCssExtractPlugin({
                    filename: '[name].css'
                }),
                new CleanWebpackPlugin([
                    `${env.OUTPUT_FOLDER}/styles.bundle.js`,
                ]),
                new CopyWebpackPlugin([
                    {from: './src/assets/img', to: 'img'}
                ]),
            ];

            if (env.LOCAL) {
                plugins = plugins.concat([
                    new HtmlWebpackPlugin({
                        hash: false,
                        template: 'src/index.html',
                        filename: 'index.html',
                        title: 'Mad Sever - Docs | Dev'
                    }),
                    new ScriptExtHtmlWebpackPlugin({
                        // defaultAttribute: 'async'
                    })
                ])
            }

            return plugins;
        })()
    }
};
