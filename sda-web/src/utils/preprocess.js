import moment from 'moment';

const DATE_FORMAT = 'YYYY-MM-DD HH:mm:ss';

function formatDateForDataList (dataList){
  dataList.forEach(data => {
    formatDateForData(data);
  });
  return dataList;
}
function formatDateForData (data) {
  data.createTime = moment(data.createdAt).format(DATE_FORMAT);
  if (!data.status) {
    data.status = true;
  };
  return data;
}

export { formatDateForDataList, formatDateForData };
