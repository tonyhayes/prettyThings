angular.module('db', [
    'adf', 'LocalStorageModule', 'ngRoute', 'oc.lazyLoad', 'dashboardControllers', 'dashboardServices'
])

    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('ajaxInterceptor');
        $httpProvider.defaults.withCredentials = true;
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
        /**
         * http://stackoverflow.com/questions/19254029/angularjs-http-post-does-not-send-data
         * The workhorse; converts an object to x-www-form-urlencoded serialization.
         * @param {Object} obj
         * @return {String}
         */
        var param = function(obj) {
            var query = '', name, value, fullSubName, subName, subValue, innerObj, i;

            for(name in obj) {
                value = obj[name];

                if(value instanceof Array) {
                    for(i=0; i<value.length; ++i) {
                        subValue = value[i];
                        fullSubName = name + '[' + i + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                }
                else if(value instanceof Object) {
                    for(subName in value) {
                        subValue = value[subName];
                        fullSubName = name + '[' + subName + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                }
                else if(value !== undefined && value !== null)
                    query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
            }

            return query.length ? query.substr(0, query.length - 1) : query;
        };

        // Override $http service's default transformRequest
        $httpProvider.defaults.transformRequest = [function(data) {
            return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
        }];
    }])

    .config(function ($routeProvider, localStorageServiceProvider, $ocLazyLoadProvider) {
        localStorageServiceProvider.setPrefix('adf');

        $routeProvider.when('/dashboard', {
            templateUrl: 'dashboard/app/partials/dashboard.html',
            controller: 'DashboardController'
        })
            .when('/dashboard/:dashboardId', {
                templateUrl: 'dashboard/app/partials/dashboard.html',
                controller: 'DashboardController'
            })
            .otherwise({
                redirectTo: '/dashboard'
            });


        $ocLazyLoadProvider.config({
            asyncLoader: $script
        })


    });

