
function getParams(url) {
  try {
    var index = url.indexOf('?');
    url = url.match(/\?([^#]+)/)[1];
    var obj = {}, arr = url.split('&');
    for (var i = 0; i < arr.length; i++) {
      var subArr = arr[i].split('=');
      var key = decodeURIComponent(subArr[0]);
      var value = decodeURIComponent(subArr[1]);
      obj[key] = value;
    }
    return obj;

  } catch (err) {
    return null;
  }
}

function parseParams(data) {
  try {
    var tempArr = [];
    for (var i in data) {
      var key = encodeURIComponent(i);
      var value = encodeURIComponent(data[i]);
      tempArr.push(key + '=' + value);
    }
    var urlParamsStr = tempArr.join('&');
    return urlParamsStr;
  } catch (err) {
    return '';
  }
}

export { getParams, parseParams };
