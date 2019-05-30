import moment from 'moment';

const DATE_FORMAT = 'YYYY-MM-DD HH:mm:ss';

function formatDateForDataList (dataList, oldAttr, newAttr){
  dataList.forEach(data => {
    formatDateForData(data, oldAttr, newAttr);
  });
  return dataList;
}
function formatDateForData (data, oldAttr, newAttr) {
  if(oldAttr == undefined || oldAttr == null){
    oldAttr = "createdAt"
  }
  if(newAttr == undefined || newAttr == null){
    newAttr = "createTime"
  }
  if(data[oldAttr] != null && data[oldAttr] != undefined)
    data[newAttr] = moment(data[oldAttr]).format(DATE_FORMAT);
  else{
    data[newAttr] = "";
  }
  return data;
}

export { formatDateForDataList, formatDateForData };
