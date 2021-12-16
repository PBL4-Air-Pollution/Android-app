# Air Quality Application's Readme

## About this project:
 
Ứng dụng theo dõi chất lượng không khí Air Quality được xây dựng nhằm giúp người dùng có thể theo dõi chất lượng không khí ở thành phố Đà Nẵng một cách trực quan, chính xác và đầy đủ nhất có thể. Người dùng có thể dễ dàng thêm, chỉnh sửa những tùy chọn theo dõi các địa điểm quan trọng trong mục Location và xem dữ liệu thống kê của từng trạm, từng ngày, từng giờ trên giao diện chính trong thời gian thực với màu sắc được đặt tương ứng với từng mốc AQI. Ngoài ra người dùng có thể sử dụng chức năng Google Map để xem vị trí các trạm, phạm vi không khí đo được, vị trí hiện tại của mình và tìm đường đi đến một địa điểm nào đó trong mục Map. Nếu muốn tìm hiểu thêm về các chất độc có trong không khí hay sự ảnh hưởng của chúng đến sức khỏe cũng như các khuyến cáo y tế cụ thể, người dùng có thể xem trong mục Infomation.

![app_logo](https://user-images.githubusercontent.com/87163945/145819848-c1805bc0-bff9-49a3-b23b-881380db1d3d.png)

## Project's components: 

### AI model predict AQI:

Chúng tôi đã train một model đơn giản bằng mô hình Kneighbors với số cluster là 25 từ một dataset thống chất lượng không khí trong năm 2016-2020 ở Ấn Độ. Kết quả thu được là một model dự đoán chỉ số AQI từ thể tích của các chất khí độc hại có trong 1 mét khối không khí mà thiết bị ở các trạm đo được, với độ chính xác khoảng 76%.

![image](https://user-images.githubusercontent.com/87163945/145823506-9662b0f9-c07d-4bce-ac01-d93385182b7b.png)

Đánh giá nồng độ của từng khí theo tiêu chuẩn AQI của Mỹ:

![image](https://user-images.githubusercontent.com/87163945/146383961-ee333c25-2376-4b79-872b-8ee6b0c65e97.png)

### Firebase realtime database: https://pbl4-airquality-default-rtdb.firebaseio.com/

Chúng tôi sử dụng Realtime Database của Firebase như một nơi đễ lưu trữ những dữ liệu chất lượng không khí trong thời gian thực. Firebase sẽ đóng vai trò như một lớp trung gian đứng giữa Android App và Server thu dữ liệu đo được từ các trạm thiết bị đo, được sử dụng để đẩy dữ liệu về database trên áp và tạo thông báo đẩy.

![image](https://user-images.githubusercontent.com/87163945/145820456-92c50e75-ec4a-43ad-b987-4b3afd4649c9.png)

### Auto generate random air quality data by hour with Python:

Điều tiếp theo cần làm là viết một đoạn chương trình Python để kết nối với Firebase, sử dụng model dự đoán AQI đã train và tạo dữ liệu chất lượng không khí, sau đó đẩy lên Firebase. Đoạn chương trình Python này sẽ được chạy 24/7 ở trên một máy ảo được chúng tôi cấu hình trên Google Cloud, đóng vai trò như một Server hoạt động 24/7, sinh ra dữ liệu chất lượng không khí theo từng giờ và thực hiện dọn dẹp dữ liệu quá cũ ở một mốc thời gian cố định.

![image](https://user-images.githubusercontent.com/87163945/145823803-12f89a74-b6d2-434e-980d-6f2b0c61e4ba.png)

### Android App:

#### Home Fragment:

Đây là nơi người dùng có thể theo dõi các dữ liệu chất lượng không khí của ứng dụng một cách trực quan và đầy đủ.

![image](https://user-images.githubusercontent.com/87163945/145825993-e58c235f-f067-426a-b663-2fdc3457de1f.png) ![image](https://user-images.githubusercontent.com/87163945/145826049-d0fa3574-70e0-4828-a951-ced87e9fbfe4.png)

Nếu chạm vào từng giờ hoặc từng ngày, người dùng có thể xem dữ liệu được thống kê một cách chi tiết và cụ thể hơn.

![image](https://user-images.githubusercontent.com/87163945/145826557-029a55d2-1bfa-4872-a844-2134cb4df212.png) ![image](https://user-images.githubusercontent.com/87163945/145826521-a6b91a8a-d41f-43ce-b13e-3ebe9b1d2c43.png)

#### Map Fragment:

Trên fragment này, người dùng có thể xem vị trí hiện tại, vị trí của các trạm đo, phạm vi đo, chỉ số AQI hiện tại ở trạm đó. Ngoài ra còn có thể tìm đường đi đến một địa điểm bất kỳ trên map. 

![image](https://user-images.githubusercontent.com/87163945/145827384-b88a36f2-b66a-430a-9496-4b238e356bfc.png) ![image](https://user-images.githubusercontent.com/87163945/145827415-fafa525d-670b-489e-9fe9-da978477f26c.png) ![image](https://user-images.githubusercontent.com/87163945/145828120-9edde4d4-e41a-4ed0-a540-d05a369cb881.png)

#### Infomation Fragment:

Ở đây người dùng có thể dành thời gian đọc và tìm hiểu thêm về các thông tin liên quan đến chất lượng không khí, các chất độc ảnh hưởng đến sức khỏe như thế nào và các khuyến cáo y tế tương ứng. 

![image](https://user-images.githubusercontent.com/87163945/145828306-2fded408-19f6-405c-b996-2f29ba33970f.png) ![image](https://user-images.githubusercontent.com/87163945/145828329-5f1221f4-f0a6-40ff-94fd-5d1a00202eaa.png)

#### Location Fragment:

Người dùng có thể đánh dấu các trạm quan trọng và đặt các nhãn tương ứng tùy theo ý muốn. Các trạm được đánh dấu sẽ được ưu tiên hiển thị thông tin trên Home Fragment và thông báo đẩy. Đặc biệt sẽ chỉ có 1 trạm được đánh dấu sao, trạm này sẽ có mức độ ưu tiên cao nhất.

![image](https://user-images.githubusercontent.com/87163945/145828792-7e05ce08-859f-48e3-8dc8-54f2d31fb320.png)  ![image](https://user-images.githubusercontent.com/87163945/145828809-a962815c-2c5b-4aca-bb97-2863131fd748.png)

#### Notification:

Ứng dụng sẽ đẩy 1 thông báo lên thiết bị Android về thông tin chất lượng không khí đo được ở một trạm nào đó. Nếu người dùng đánh dấu sao trên một trạm ở Location Fragment, thông báo sẽ cập nhật theo thông tin của trạm đó, nếu không có trạm nào được đánh dấu sao thì ứng dụng sẽ chọn trạm có vị trí gần người dùng nhất để hiển thị.

![image](https://user-images.githubusercontent.com/87163945/145826638-20b3dd1e-44fb-40d9-ac49-c34245309401.png)

(Lưu ý: Ứng dụng có thể sẽ mất một vài phút để cập nhật toàn bộ dữ liệu trên Firebase trong lần chạy đầu tiên)
 
## Tài liệu tham khảo:

1. Giải thích chung về air pollution: https://www.youtube.com/watch?v=Tds3k97aAzo

2. Khóa học về data science và AI model trainning: https://www.youtube.com/watch?v=HPGYTWYM13s&list=PLJcWUrckOCKKwjjHALg6fnyQCHv8z92rs&index=1

3. Github repo handbook for python machine learning library: https://github.com/CodexploreRepo/data_science

4. Chất lượng không khí ở Đà Năng:
   https://www.iqair.com/vi/vietnam/da-nang?fbclid=IwAR04sBwvZHPRk2-N4lKG5TgkTr5tXb8T1wu0LlP6cZNfzdT-HsRId537t5E

5. Đà Nẵng AQI update theo giờ: http://enviinfo.cem.gov.vn/?fbclid=IwAR3ejJd7Ptn2UwRmk5FYTWpdWlTqECqRCKKYBndgC-eYEEBtNUOpqsnLS7M

6. Cách tính AQI:
   http://www.tapchimoitruong.vn/phap-luat--chinh-sach-16/H%C6%B0%E1%BB%9Bng-d%E1%BA%ABn-m%E1%BB%9Bi-v%E1%BB%81-c%C3%A1ch-t%C3%ADnh-ch%E1%BB%89-s%E1%BB%91-ch%E1%BA%A5t-l%C6%B0%E1%BB%A3ng-kh%C3%B4ng-kh%C3%AD-c%E1%BB%A7a-Vi%E1%BB%87t-Nam-(VN_AQI-)-18414
